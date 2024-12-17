package ru.restaurant.agents;

import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.DFService;
import jade.lang.acl.ACLMessage;
import ru.restaurant.agents.base.BaseAgent;
import ru.restaurant.model.resource.Product;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ProductAgent extends BaseAgent {
    private static final String AGENT_TYPE = "продукт";
    private Product product;
    private double reservedQuantity = 0;

    @Override
    protected void setup() {
        super.setup();
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            product = (Product) args[0];
        }
        addBehaviour(new ProductRequestBehaviour());
        log.info("Продукт-агент {} готов.", getAID().getName());
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

    private class ProductRequestBehaviour extends CyclicBehaviour {
        @Override
        public void action() {
            ACLMessage msg = myAgent.receive();
            if (msg != null) {
                // Обработка запросов на резервирование/использование продукта
            } else {
                block();
            }
        }
    }
}