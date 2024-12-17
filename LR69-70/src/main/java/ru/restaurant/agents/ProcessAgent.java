package ru.restaurant.agents;

import jade.core.behaviours.SequentialBehaviour;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.DFService;
import ru.restaurant.agents.base.BaseAgent;
import ru.restaurant.model.process.Process;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProcessAgent extends BaseAgent {
    private static final String AGENT_TYPE = "процесс";
    private Process process;

    @Override
    protected void setup() {
        super.setup();
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            // Инициализация процесса
        }

        SequentialBehaviour processExecution = new SequentialBehaviour(this);
        // Добавляем шаги выполнения процесса
        addBehaviour(processExecution);

        log.info("Технологический агент {} готов.", getAID().getName());
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

    @Override
    protected String getAgentType() {
        return null;
    }
}