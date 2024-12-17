package ru.restaurant.model.menu;

import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Data
public class Menu {
    private Map<Integer, MenuItem> items = new ConcurrentHashMap<>();

    public void addItem(MenuItem item) {
        items.put(item.getId(), item);
    }

    public void removeItem(int itemId) {
        items.remove(itemId);
    }

    public List<MenuItem> getActiveItems() {
        return items.values().stream()
                .filter(MenuItem::isActive)
                .collect(Collectors.toList());
    }

    public void updateItemAvailability(int itemId, boolean available) {
        MenuItem item = items.get(itemId);
        if (item != null) {
            item.setActive(available);
        }
    }
}