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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class CookAgent extends BaseAgent {
    private static final String AGENT_TYPE = "повар";
    private boolean busy = false;
    private Map<String, String> currentOrders = new ConcurrentHashMap<>();
    private AID equipmentAID;

    @Override
    protected String getAgentType() {
        return AGENT_TYPE;
    }

    @Override
    protected void setup() {
        super.setup();
        findEquipment();
        addBehaviour(new CookingBehaviour());
        log.info("Агент повара {} готов.", getAID().getName());
    }

    private void findEquipment() {
        try {
            DFAgentDescription template = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setType("оборудование");  // проверяем, что тип совпадает с тем, что указан в EquipmentAgent
            template.addServices(sd);
            DFAgentDescription[] result = DFService.search(this, template);
            if (result.length > 0) {
                equipmentAID = result[0].getName();
                log.info("Найдено оборудование: {}", equipmentAID.getLocalName());
            } else {
                log.error("Оборудование не найдено в системе!");
                findEquipment(); // Повторная попытка поиска
            }
        } catch (FIPAException e) {
            log.error("Ошибка при поиске оборудования", e);
        }
    }

    private class CookingBehaviour extends CyclicBehaviour {
        @Override
        public void action() {
            ACLMessage msg = receive();
            if (msg != null) {
                String content = msg.getContent();
                if (!busy || content.startsWith("ОБОРУДОВАНИЕ_ГОТОВО")) {
                    processMessage(msg);
                }
            } else {
                block();
            }
        }

        protected void processMessage(ACLMessage msg) {
            log.info("=== Повар получил сообщение: {} ===", msg.getContent());
            String content = msg.getContent();
            if (content.startsWith("ГОТОВИТЬ_ЗАКАЗ") && !busy) {
                handleNewCookingTask(msg);
            } else if (content.startsWith("ОБОРУДОВАНИЕ_ГОТОВО")) {
                handleEquipmentReady(msg);
            }
        }

        private void handleNewCookingTask(ACLMessage msg) {
            try {
                String orderId = msg.getContent().split(":")[1];
                if (!currentOrders.containsKey(orderId)) {
                    busy = true;
                    currentOrders.put(orderId, "ГОТОВИТСЯ");
                    log.info("Начинаем готовить заказ {}", orderId);

                    if (equipmentAID != null) {
                        ACLMessage equipMsg = new ACLMessage(ACLMessage.REQUEST);
                        equipMsg.addReceiver(equipmentAID);
                        equipMsg.setContent("ИСПОЛЬЗОВАТЬ_ОБОРУДОВАНИЕ:" + orderId);
                        send(equipMsg);
                        log.info("Запрошено оборудование для заказа {}", orderId);
                    } else {
                        log.error("Оборудование не найдено!");
                        startCooking(orderId);
                    }
                }
            } catch (Exception e) {
                log.error("Ошибка при обработке задачи готовки", e);
                busy = false;
            }
        }

        private void handleEquipmentReady(ACLMessage msg) {
            String orderId = msg.getContent().split(":")[1];
            if (currentOrders.containsKey(orderId)) {
                startCooking(orderId);
            }
        }

        private void startCooking(String orderId) {
            new Thread(() -> {
                try {
                    log.info("Начало приготовления заказа {}", orderId);
                    Thread.sleep(5000); // 5 секунд на готовку
                    finishCooking(orderId);
                } catch (InterruptedException e) {
                    log.error("Приготовление прервано", e);
                    busy = false;
                }
            }).start();
        }

        private void finishCooking(String orderId) {
            try {
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.addReceiver(new AID("Руководитель", AID.ISLOCALNAME));
                msg.setContent("ЗАКАЗ_ГОТОВ:" + orderId);
                send(msg);

                currentOrders.remove(orderId);
                busy = false;
                log.info("Заказ {} готов", orderId);
            } catch (Exception e) {
                log.error("Ошибка при завершении готовки", e);
                busy = false;
            }
        }
    }
}