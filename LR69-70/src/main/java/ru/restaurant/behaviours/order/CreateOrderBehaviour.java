package ru.restaurant.behaviours.order;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import ru.restaurant.agents.OrderAgent;
import ru.restaurant.model.order.Order;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CreateOrderBehaviour extends OneShotBehaviour {
    private final Order order;

    public CreateOrderBehaviour(OrderAgent agent, Order order) {
        super(agent);
        this.order = order;
    }

    @Override
    public void action() {
        try {
            // Логика создания заказа
            log.info("Creating order: {}", order);

            // Отправка сообщения о создании заказа
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setContent("Order created: " + order.getId());
            // Добавление получателей
            myAgent.send(msg);

        } catch (Exception e) {
            log.error("Error creating order", e);
        }
    }
}