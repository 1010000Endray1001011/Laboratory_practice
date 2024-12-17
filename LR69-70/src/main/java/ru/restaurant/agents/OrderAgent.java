package ru.restaurant.agents;

import jade.core.AID;
import jade.core.behaviours.FSMBehaviour;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.DFService;
import ru.restaurant.agents.base.BaseAgent;
import ru.restaurant.model.order.Order;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class OrderAgent extends BaseAgent {
    private static final String AGENT_TYPE = "заказ";
    private Order order;
    private AID visitorAID;

    @Override
    protected void setup() {
        super.setup();
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            // Инициализация заказа
        }

        // Добавляем поведение обработки заказа
        FSMBehaviour orderProcessing = new FSMBehaviour(this);
        // Добавляем состояния и переходы
        addBehaviour(orderProcessing);

        log.info("Агент заказа {} готов.", getAID().getName());
    }

    @Override
    protected void registerInDF() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType(AGENT_TYPE);
        sd.setName(getLocalName());
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (Exception e) {
            log.error("Ошибка при регистрации в DF", e);
        }
    }
}