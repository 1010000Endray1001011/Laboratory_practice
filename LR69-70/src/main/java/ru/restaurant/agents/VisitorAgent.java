package ru.restaurant.agents;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import ru.restaurant.agents.base.BaseAgent;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class VisitorAgent extends BaseAgent {
    private static final String AGENT_TYPE = "посетитель";
    private Map<String, String> myOrders = new ConcurrentHashMap<>();

    @Override
    protected String getAgentType() {
        return AGENT_TYPE;
    }

    @Override
    protected void setup() {
        super.setup();
        addBehaviour(new OrderTrackingBehaviour());
        log.info("Агент для посетителей {} готов.", getAID().getName());
    }

    private class OrderTrackingBehaviour extends CyclicBehaviour {
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
            String content = msg.getContent();
            if (content.startsWith("ЗАКАЗ_ПРИНЯТ")) {
                handleOrderAccepted(msg);
            } else if (content.startsWith("ЗАКАЗ_ГОТОВ")) {
                handleOrderReady(msg);
            } else if (content.startsWith("ЗАКАЗ_ОТМЕНЁН")) {
                handleOrderCancelled(msg);
            }
        }

        private void handleOrderAccepted(ACLMessage msg) {
            String orderId = msg.getContent().split(":")[1];
            myOrders.put(orderId, "ПРИНЯТО");
            log.info("Заказ {} был принят", orderId);

            // Подтверждаем получение
            ACLMessage reply = msg.createReply();
            reply.setContent("ЗАКАЗ_ПРИНЯТ_АКЦИЕЙ:" + orderId);
            send(reply);
        }

        private void handleOrderReady(ACLMessage msg) {
            String orderId = msg.getContent().split(":")[1];
            myOrders.put(orderId, "ГОТОВЫЙ");
            log.info("Заказ {} готов к отправке", orderId);

            // Подтверждаем получение
            ACLMessage reply = msg.createReply();
            reply.setContent("ЗАКАЗ_ПОЛУЧЕН:" + orderId);
            send(reply);
        }

        private void handleOrderCancelled(ACLMessage msg) {
            String[] parts = msg.getContent().split(":");
            String orderId = parts[1];
            String reason = parts[2];
            myOrders.remove(orderId);
            log.info("Заказ {} был отменен. Причина: {}", orderId, reason);

            // Подтверждаем получение информации об отмене
            ACLMessage reply = msg.createReply();
            reply.setContent("ОТМЕНА_ЗАКАЗА:" + orderId);
            send(reply);
        }
    }
}