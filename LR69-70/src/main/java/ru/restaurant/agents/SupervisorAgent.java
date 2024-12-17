package ru.restaurant.agents;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import ru.restaurant.agents.base.BaseAgent;
import ru.restaurant.model.order.Order;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SupervisorAgent extends BaseAgent {
    private static final String AGENT_TYPE = "руководитель";
    private final Map<String, Order> activeOrders = new ConcurrentHashMap<>();
    private AID warehouseAID;

    @Override
    protected String getAgentType() {
        return AGENT_TYPE;
    }

    @Override
    protected void setup() {
        super.setup();
        findWarehouse();
        addBehaviour(new OrderManagementBehaviour());
        log.info("Агент руководителя {} готов.", getAID().getName());
    }

    private void findWarehouse() {
        try {
            DFAgentDescription template = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setType("склад");  // изменено с "warehouse"
            template.addServices(sd);

            log.info("Поиск склада...");
            DFAgentDescription[] result = DFService.search(this, template);

            if (result != null && result.length > 0) {
                warehouseAID = result[0].getName();
                log.info("Найден склад: {}", warehouseAID.getName());
            } else {
                log.error("Склад не найден в DF!");
            }
        } catch (FIPAException e) {
            log.error("Ошибка при поиске склада: ", e);
        }
    }

    private class OrderManagementBehaviour extends CyclicBehaviour {
        @Override
        public void action() {
            ACLMessage msg = receive();
            if (msg != null) {
                log.info("Получено сообщение: {}", msg.getContent());
                processMessage(msg);
            } else {
                block();
            }
        }

        private void processMessage(ACLMessage msg) {
            String content = msg.getContent();
            if (content.startsWith("НОВЫЙ_ЗАКАЗ")) {
                handleNewOrder(msg);
            } else if (content.startsWith("ИНГРЕДИЕНТЫ_ДОСТУПНЫ")) {
                handleIngredientsAvailable(msg);
            } else if (content.startsWith("ИНГРЕДИЕНТЫ_ОТСУТСТВУЮТ")) {
                handleIngredientsNotAvailable(msg);
            } else if (content.startsWith("ЗАКАЗ_ГОТОВ")) {
                handleOrderReady(msg);
            } else if (content.startsWith("ЗАКАЗ_ПОЛУЧЕН")) {
                handleOrderReceived(msg);
            }
        }

        private void handleNewOrder(ACLMessage msg) {
            try {
                log.info("=== Начало обработки нового заказа ===");
                String[] orderParts = msg.getContent().split(":");
                String item = orderParts[1];
                int quantity = Integer.parseInt(orderParts[2]);
                log.info("Разбор сообщения: товар = {}, количество = {}", item, quantity);

                // Проверяем warehouseAID
                log.info("Проверка состояния склада: warehouseAID = {}",
                        warehouseAID != null ? warehouseAID.getName() : "null");

                if (warehouseAID == null) {
                    log.info("Склад не найден, пытаемся найти снова...");
                    findWarehouse();
                    if (warehouseAID == null) {
                        log.error("Склад не найден после повторной попытки!");
                        return;
                    }
                }

                // Создаем заказ
                log.info("Создание нового заказа...");
                Order order = new Order();
                order.setId(UUID.randomUUID().toString());
                order.setStatus("НОВЫЙ");
                order.setVisitorAID(msg.getSender());
                order.setSupervisorAID(getAID());
                order.setOrderStarted(LocalDateTime.now());

                Order.OrderItem orderItem = new Order.OrderItem();
                orderItem.setName(item);
                orderItem.setQuantity(quantity);
                orderItem.setStatus("НОВЫЙ");
                order.getItems().add(orderItem);

                activeOrders.put(order.getId(), order);
                log.info("Заказ создан с ID: {}", order.getId());

                // Отправляем запрос на склад
                log.info("Подготовка запроса на склад...");
                ACLMessage warehouseMsg = new ACLMessage(ACLMessage.REQUEST);
                warehouseMsg.addReceiver(warehouseAID);
                warehouseMsg.setContent("ПРОВЕРКА_ИНГРЕДИЕНТОВ:" + item + ":" + quantity);
                log.info("Отправка запроса на склад: {}", warehouseMsg.getContent());
                send(warehouseMsg);
                log.info("Запрос отправлен на склад");

            } catch (Exception e) {
                log.error("Ошибка при обработке заказа", e);
                e.printStackTrace();
            }
        }

        private void handleIngredientsAvailable(ACLMessage msg) {
            String[] parts = msg.getContent().split(":");
            String itemName = parts[1];

            // Находим заказ по названию товара
            Order order = activeOrders.values().stream()
                    .filter(o -> o.getItems().stream()
                            .anyMatch(item -> item.getName().equals(itemName) &&
                                    "НОВЫЙ".equals(item.getStatus())))
                    .findFirst()
                    .orElse(null);

            if (order != null && "НОВЫЙ".equals(order.getStatus())) {
                order.setStatus("ГОТОВИТСЯ");
                // Находим повара
                try {
                    DFAgentDescription template = new DFAgentDescription();
                    ServiceDescription sd = new ServiceDescription();
                    sd.setType("повар");
                    template.addServices(sd);
                    DFAgentDescription[] result = DFService.search(myAgent, template);
                    if (result.length > 0) {
                        AID cookAID = result[0].getName();
                        ACLMessage cookMsg = new ACLMessage(ACLMessage.REQUEST);
                        cookMsg.addReceiver(cookAID);
                        cookMsg.setContent("ГОТОВИТЬ_ЗАКАЗ:" + order.getId());
                        send(cookMsg);
                        log.info("Заказ {} передан повару {}", order.getId(), cookAID.getLocalName());
                    } else {
                        log.error("Не найдено доступных поваров!");
                    }
                } catch (FIPAException e) {
                    log.error("Ошибка при поиске повара", e);
                }
            }
        }

        private void handleIngredientsNotAvailable(ACLMessage msg) {
            String[] parts = msg.getContent().split(":");
            String itemName = parts[1];

            // Находим заказ
            Order order = activeOrders.values().stream()
                    .filter(o -> o.getItems().stream()
                            .anyMatch(item -> item.getName().equals(itemName)))
                    .findFirst()
                    .orElse(null);

            if (order != null) {
                order.setStatus("ОТМЕНЁННЫЙ");
                ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
                reply.addReceiver(order.getVisitorAID());
                reply.setContent("ЗАКАЗ_ ОТМЕНЕН" + order.getId() + ":ингредиенты, которых нет в наличии");
                send(reply);
                activeOrders.remove(order.getId());
                log.info("Заказ {} отменен из-за отсутствия ингредиентов", order.getId());
            }
        }

        private void handleOrderReady(ACLMessage msg) {
            String orderId = msg.getContent().split(":")[1];
            Order order = activeOrders.get(orderId);
            if (order != null) {
                order.setStatus("ГОТОВЫЙ");
                order.setOrderEnded(LocalDateTime.now());

                ACLMessage visitorMsg = new ACLMessage(ACLMessage.INFORM);
                visitorMsg.addReceiver(order.getVisitorAID());
                visitorMsg.setContent("ЗАКАЗ_ГОТОВ:" + orderId);
                send(visitorMsg);
                log.info("Заказ {} готов, и посетитель был уведомлен об этом", orderId);
            }
        }

        private void handleOrderReceived(ACLMessage msg) {
            String orderId = msg.getContent().split(":")[1];
            Order order = activeOrders.remove(orderId);
            if (order != null) {
                log.info("Заказ {} был получен посетителем", orderId);
            }
        }
    }
}