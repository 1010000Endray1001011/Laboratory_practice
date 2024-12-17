package ru.restaurant.agents;

import jade.core.AID;
import jade.core.behaviours.FSMBehaviour;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.DFService;
import ru.restaurant.agents.base.BaseAgent;
import ru.restaurant.model.process.Operation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class OperationAgent extends BaseAgent {
    private static final String AGENT_TYPE = "операция";
    private Operation operation;
    private AID cookAID;
    private AID equipmentAID;

    @Override
    protected void setup() {
        super.setup();
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            operation = (Operation) args[0];
        }

        FSMBehaviour operationExecution = new FSMBehaviour(this) {
            @Override
            public int onEnd() {
                myAgent.doDelete();
                return super.onEnd();
            }
        };

        addBehaviour(operationExecution);
        log.info("Операционный агент {} создан для выполнения операции {}", getAID().getName(), operation.getId());
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

    public void assignCook(AID cookAID) {
        this.cookAID = cookAID;
        log.info("Повар {}, назначенный на работу {}", cookAID.getLocalName(), operation.getId());
    }

    public void assignEquipment(AID equipmentAID) {
        this.equipmentAID = equipmentAID;
        log.info("Оборудование {}, назначенное для эксплуатации {}", equipmentAID.getLocalName(), operation.getId());
    }
}