package ru.restaurant.behaviours.process;

import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import ru.restaurant.agents.ProcessAgent;
import ru.restaurant.model.process.Operation;
import ru.restaurant.model.process.Process;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class ExecuteProcessBehaviour extends FSMBehaviour {
    private static final String STATE_INIT = "INIT";
    private static final String STATE_RESERVE_RESOURCES = "RESERVE_RESOURCES";
    private static final String STATE_EXECUTE_OPERATIONS = "EXECUTE_OPERATIONS";
    private static final String STATE_COMPLETE = "COMPLETE";

    public ExecuteProcessBehaviour(ProcessAgent agent, Process process) {
        super(agent);

        // Регистрация состояний
        registerFirstState(new InitializeState(process), STATE_INIT);
        registerState(new ReserveResourcesState(process), STATE_RESERVE_RESOURCES);
        registerState(new ExecuteOperationsState(process), STATE_EXECUTE_OPERATIONS);
        registerLastState(new CompleteState(process), STATE_COMPLETE);

        // Регистрация переходов
        registerDefaultTransition(STATE_INIT, STATE_RESERVE_RESOURCES);
        registerDefaultTransition(STATE_RESERVE_RESOURCES, STATE_EXECUTE_OPERATIONS);
        registerDefaultTransition(STATE_EXECUTE_OPERATIONS, STATE_COMPLETE);
    }

    private class InitializeState extends OneShotBehaviour {
        private final Process process;

        public InitializeState(Process process) {
            this.process = process;
        }

        @Override
        public void action() {
            log.info("Initializing process {}", process.getId());
            process.setStatus("INITIALIZED");
        }
    }

    private class ReserveResourcesState extends OneShotBehaviour {
        private final Process process;

        public ReserveResourcesState(Process process) {
            this.process = process;
        }

        @Override
        public void action() {
            log.info("Reserving resources for process {}", process.getId());
            // Логика резервирования ресурсов
        }
    }

    private class ExecuteOperationsState extends OneShotBehaviour {
        private final Process process;

        public ExecuteOperationsState(Process process) {
            this.process = process;
        }

        @Override
        public void action() {
            log.info("Executing operations for process {}", process.getId());
            for (Operation operation : process.getOperations()) {
                // Отправка запроса на выполнение операции
                ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                // Добавление получателей (повара, оборудование)
                msg.setContent("EXECUTE_OPERATION:" + operation.getId());
                myAgent.send(msg);
            }
        }
    }

    private class CompleteState extends OneShotBehaviour {
        private final Process process;

        public CompleteState(Process process) {
            this.process = process;
        }

        @Override
        public void action() {
            log.info("Completing process {}", process.getId());
            process.setStatus("COMPLETED");
            process.setEnded(LocalDateTime.now());

            // Уведомление о завершении процесса
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setContent("PROCESS_COMPLETED:" + process.getId());
            myAgent.send(msg);
        }
    }
}