package ru.restaurant.behaviours.order;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import ru.restaurant.agents.OrderAgent;
import ru.restaurant.model.order.Order;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UpdateOrderStatusBehaviour extends OneShotBehaviour {
    private final String newStatus;
    private final Order order;

    public UpdateOrderStatusBehaviour(OrderAgent agent, Order order, String newStatus) {
        super(agent);
        this.order = order;
        this.newStatus = newStatus;
    }

    @Override
    public void action() {
        try {
            order.setStatus(newStatus);
            log.info("Статус заказа {} обновлен до {}", order.getId(), newStatus);

            // Уведомление клиента
            ACLMessage notification = new ACLMessage(ACLMessage.INFORM);
            notification.addReceiver(order.getVisitorAID());
            notification.setContent("ЗАКАЗ_СТАТУС_ОБНОВЛЕНИЯ" + order.getId() + ":" + newStatus);
            myAgent.send(notification);

            // Если заказ завершен, уведомляем супервизора
            if (newStatus.equals("ЗАВЕРШЁННЫЙ") || newStatus.equals("ОТМЕНЁННЫЙ")) {
                ACLMessage supervisorMsg = new ACLMessage(ACLMessage.INFORM);
                supervisorMsg.addReceiver(order.getSupervisorAID());
                supervisorMsg.setContent("ЗАКАЗ_ ВЫПОЛНЕН:" + order.getId());
                myAgent.send(supervisorMsg);
            }
        } catch (Exception e) {
            log.error("Ошибка при обновлении статуса заказа", e);
        }
    }
}