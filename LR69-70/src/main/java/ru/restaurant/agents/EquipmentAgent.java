package ru.restaurant.agents;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import ru.restaurant.agents.base.BaseAgent;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class EquipmentAgent extends BaseAgent {
    private static final String AGENT_TYPE = "оборудование";
    private boolean inUse = false;
    private Map<String, String> currentTasks = new ConcurrentHashMap<>();

    @Override
    protected String getAgentType() {
        return AGENT_TYPE;
    }

    @Override
    protected void setup() {
        super.setup();
        addBehaviour(new EquipmentBehaviour());
        log.info("Агент по оборудованию {} готов.", getAID().getName());
    }

    private class EquipmentBehaviour extends CyclicBehaviour {
        @Override
        public void action() {
            ACLMessage msg = receive();
            if (msg != null) {
                processMessage(msg);
            } else {
                block();
            }
        }

        protected void processMessage(ACLMessage msg) {
            log.info("=== Оборудование получило сообщение: {} ===", msg.getContent());
            String content = msg.getContent();
            if (content.startsWith("ИСПОЛЬЗОВАТЬ_ОБОРУДОВАНИЕ") && !inUse) {
                handleUseEquipment(msg);
            }
        }

        private void handleUseEquipment(ACLMessage msg) {
            try {
                String orderId = msg.getContent().split(":")[1];
                if (!currentTasks.containsKey(orderId)) {
                    inUse = true;
                    currentTasks.put(orderId, "ИСПОЛЬЗУЕТСЯ");
                    log.info("Начало использования оборудования для заказа {}", orderId);

                    new Thread(() -> {
                        try {
                            Thread.sleep(3000); // 3 секунды работы
                            finishUsingEquipment(orderId, msg.getSender());
                        } catch (InterruptedException e) {
                            log.error("Работа оборудования прервана", e);
                            inUse = false;
                        }
                    }).start();
                }
            } catch (Exception e) {
                log.error("Ошибка при использовании оборудования", e);
                inUse = false;
            }
        }

        private void handleStopEquipment(ACLMessage msg) {
            String orderId = msg.getContent().split(":")[1];
            if (currentTasks.containsKey(orderId)) {
                currentTasks.remove(orderId);
                inUse = false;
                log.info("Оборудование остановлено для заказа {}", orderId);
            }
        }

        private void finishUsingEquipment(String orderId, jade.core.AID sender) {
            try {
                ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
                reply.addReceiver(sender);
                reply.setContent("ОБОРУДОВАНИЕ_ГОТОВО:" + orderId);
                send(reply);

                currentTasks.remove(orderId);
                inUse = false;
                log.info("Оборудование завершило работу для заказа {}", orderId);
            } catch (Exception e) {
                log.error("Ошибка при завершении работы оборудования", e);
                inUse = false;
            }
        }
    }
}