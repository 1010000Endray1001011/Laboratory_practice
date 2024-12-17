package ru.restaurant.service;

import ru.restaurant.model.process.Process;
import ru.restaurant.model.process.Operation;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ProcessService {
    private final Map<Integer, Process> activeProcesses = new ConcurrentHashMap<>();

    public Process createProcess(int dishId) {
        Process process = loadProcessTemplate(dishId);
        activeProcesses.put(process.getId(), process);
        return process;
    }

    public void updateProcessStatus(int processId, String status) {
        Process process = activeProcesses.get(processId);
        if (process != null) {
            process.setStatus(status);
            log.info("Updated process {} status to {}", processId, status);
        }
    }

    private Process loadProcessTemplate(int dishId) {
        // Загрузка шаблона процесса из конфигурации
        return new Process();
    }

    public List<Operation> getParallelOperations(Process process) {
        // Получение списка параллельных операций
        return List.of();
    }
}