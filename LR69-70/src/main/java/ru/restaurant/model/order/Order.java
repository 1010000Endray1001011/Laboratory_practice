package ru.restaurant.model.order;

import lombok.Data;
import jade.core.AID;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Data
public class Order {
    private String id;
    private String visitorName;
    private AID visitorAID;
    private AID supervisorAID;
    private LocalDateTime orderStarted;
    private LocalDateTime orderEnded;
    private LocalDateTime estimatedCompletionTime;
    private double orderTotal;
    private String status;
    private List<OrderItem> items = new ArrayList<>();

    @Data
    public static class OrderItem {
        private int itemId;
        private String name;
        private int quantity;
        private double price;
        private String status;
    }
}