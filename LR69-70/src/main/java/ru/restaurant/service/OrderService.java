package ru.restaurant.service;

import ru.restaurant.dto.OrderDto;
import ru.restaurant.model.order.Order;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class OrderService {
    private final Map<String, Order> activeOrders = new ConcurrentHashMap<>();
    private final MenuService menuService;

    public OrderService(MenuService menuService) {
        this.menuService = menuService;
    }

    public Order createOrder(OrderDto orderDto) {
        Order order = convertDtoToOrder(orderDto);
        activeOrders.put(order.getId(), order);
        log.info("Created new order: {}", order.getId());
        return order;
    }

    public void updateOrderStatus(String orderId, String status) {
        Order order = activeOrders.get(orderId);
        if (order != null) {
            order.setStatus(status);
            log.info("Updated order {} status to {}", orderId, status);
        }
    }

    private Order convertDtoToOrder(OrderDto dto) {
        // Конвертация DTO в модель
        return new Order();
    }
}