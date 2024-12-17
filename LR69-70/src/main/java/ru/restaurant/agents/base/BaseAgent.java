package ru.restaurant.agents.base;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseAgent extends Agent {

    @Override
    protected void setup() {
        registerInDF();
    }

    protected void registerInDF() {
        try {
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName(getAID());
            ServiceDescription sd = new ServiceDescription();
            sd.setType(getAgentType());
            sd.setName(getLocalName());
            dfd.addServices(sd);

            // Проверка существующей регистрации
            DFAgentDescription[] result = DFService.search(this, dfd);
            if (result.length > 0) {
                DFService.deregister(this);
            }
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            log.error("Ошибка при регистрации в DF", e);
        }
    }

    protected abstract String getAgentType();

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            log.error("Ошибка при отмене регистрации в DF", e);
        }
    }
}