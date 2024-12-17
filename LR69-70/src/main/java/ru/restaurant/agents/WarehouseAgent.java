package ru.restaurant.agents;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import ru.restaurant.agents.base.BaseAgent;
import ru.restaurant.model.resource.Product;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.time.LocalDateTime;

@Slf4j
public class WarehouseAgent extends BaseAgent {
    private static final String AGENT_TYPE = "склад";
    private Map<Integer, Product> inventory = new ConcurrentHashMap<>();

    @Override
    protected String getAgentType() {
        return AGENT_TYPE;
    }

    @Override
    protected void setup() {
        super.setup();
        loadInitialInventory();
        addBehaviour(new InventoryManagementBehaviour());
        log.info("Агент склада {} готов.", getAID().getName());
    }

    private void loadInitialInventory() {
        // Чай
        Product tea = new Product();
        tea.setProductId(1);
        tea.setName("чай");
        tea.setQuantity(10);
        tea.setUnit("штук");
        tea.setDeliveredAt(LocalDateTime.now());
        tea.setValidUntil(LocalDateTime.now().plusMonths(6));
        inventory.put(tea.getProductId(), tea);

        // Кофе
        Product coffee = new Product();
        coffee.setProductId(2);
        coffee.setName("кофе");
        coffee.setQuantity(15);
        coffee.setUnit("штук");
        coffee.setDeliveredAt(LocalDateTime.now());
        coffee.setValidUntil(LocalDateTime.now().plusMonths(12));
        inventory.put(coffee.getProductId(), coffee);

        // Сахар
        Product sugar = new Product();
        sugar.setProductId(3);
        sugar.setName("сахар");
        sugar.setQuantity(100);
        sugar.setUnit("штук");
        sugar.setDeliveredAt(LocalDateTime.now());
        sugar.setValidUntil(LocalDateTime.now().plusYears(1));
        inventory.put(sugar.getProductId(), sugar);

        // Молоко
        Product milk = new Product();
        milk.setProductId(4);
        milk.setName("молоко");
        milk.setQuantity(20);
        milk.setUnit("литров");
        milk.setDeliveredAt(LocalDateTime.now());
        milk.setValidUntil(LocalDateTime.now().plusDays(7));
        inventory.put(milk.getProductId(), milk);
    }

    private class InventoryManagementBehaviour extends CyclicBehaviour {
        @Override
        public void action() {
            ACLMessage msg = receive();
            if (msg != null) {
                processMessage(msg);
            } else {
                block();
            }
        }

        private void processMessage(ACLMessage msg) {
            log.info("=== Склад получил сообщение: {} ===", msg.getContent());
            String content = msg.getContent();
            if (content.startsWith("ПРОВЕРКА_ИНГРЕДИЕНТОВ")) {
                log.info("Начинаем проверку ингредиентов");
                handleCheckIngredients(msg);
            }
        }

        private void handleCheckIngredients(ACLMessage msg) {
            try {
                String[] parts = msg.getContent().split(":");
                String itemName = parts[1];
                double quantity = Double.parseDouble(parts[2]);

                log.info("Проверка наличия ингредиента {} в количестве {}", itemName, quantity);

                Product product = inventory.values().stream()
                        .filter(p -> p.getName().equals(itemName))
                        .filter(p -> p.canReserve(quantity))
                        .findFirst()
                        .orElse(null);

                ACLMessage reply = msg.createReply();
                if (product != null) {
                    reply.setContent("ИНГРЕДИЕНТЫ_ДОСТУПНЫ:" + itemName);
                    log.info("Ингредиент {} доступен в количестве {} {}",
                            itemName, quantity, product.getUnit());
                } else {
                    reply.setContent("ИНГРЕДИЕНТЫ_ОТСУТСТВУЮТ:" + itemName);
                    log.warn("Недостаточно ингредиента {} (требуется: {})",
                            itemName, quantity);
                }
                send(reply);
                log.info("Отправлен ответ: {}", reply.getContent());
            } catch (Exception e) {
                log.error("Ошибка при проверке ингредиентов", e);
                ACLMessage reply = msg.createReply();
                reply.setContent("ERROR:check_ingredients_failed");
                send(reply);
            }
        }

        private void handleReserveIngredients(ACLMessage msg) {
            try {
                String[] parts = msg.getContent().split(":");
                String itemName = parts[1];
                double quantity = Double.parseDouble(parts[2]);

                Product product = inventory.values().stream()
                        .filter(p -> p.getName().equals(itemName))
                        .filter(p -> p.canReserve(quantity))
                        .findFirst()
                        .orElse(null);

                ACLMessage reply = msg.createReply();
                if (product != null) {
                    product.reserve(quantity);
                    reply.setContent("ИНГРЕДИЕНТЫ ДЛЯ РЕЗЕРВИРОВАНИЯ:" + itemName);
                    log.info("Зарезервировано {} {} {}",
                            quantity, product.getUnit(), itemName);
                } else {
                    reply.setContent("ОШИБКА_РЕЗЕРВИРОВАНИЯ:" + itemName);
                    log.warn("Не удалось зарезервировать {} {}",
                            quantity, itemName);
                }
                send(reply);
            } catch (Exception e) {
                log.error("Ошибка при резервировании ингредиентов", e);
            }
        }

        private void handleReleaseIngredients(ACLMessage msg) {
            try {
                String[] parts = msg.getContent().split(":");
                String itemName = parts[1];
                double quantity = Double.parseDouble(parts[2]);

                Product product = inventory.values().stream()
                        .filter(p -> p.getName().equals(itemName))
                        .findFirst()
                        .orElse(null);

                ACLMessage reply = msg.createReply();
                if (product != null) {
                    product.setReservedQuantity(
                            Math.max(0, product.getReservedQuantity() - quantity)
                    );
                    reply.setContent("ИНГРЕДИЕНТЫ, КОТОРЫЕ БЫЛИ ВЫПУЩЕНЫ ПОВТОРНО:" + itemName);
                    log.info("Освобождено {} {} {}",
                            quantity, product.getUnit(), itemName);
                } else {
                    reply.setContent("ОШИБКА_ВЫПУСКА:" + itemName);
                    log.warn("Не удалось освободить {} {}",
                            quantity, itemName);
                }
                send(reply);
            } catch (Exception e) {
                log.error("Ошибка при освобождении ингредиентов", e);
            }
        }
    }
}