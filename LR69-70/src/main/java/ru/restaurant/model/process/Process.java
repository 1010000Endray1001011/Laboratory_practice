package ru.restaurant.model.process;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Data
public class Process {
    private int id;
    private int ordDish;
    private LocalDateTime started;
    private LocalDateTime ended;
    private boolean active;
    private List<Operation> operations = new ArrayList<>();
    private String status;

    public void addOperation(Operation operation) {
        operations.add(operation);
    }

    public List<Operation> getParallelOperations(int asyncPoint) {
        return operations.stream()
                .filter(op -> op.getOperAsyncPoint() == asyncPoint)
                .collect(Collectors.toList());  // вместо .toList()
    }

    public boolean isCompleted() {
        return operations.stream().allMatch(Operation::isCompleted);
    }
}