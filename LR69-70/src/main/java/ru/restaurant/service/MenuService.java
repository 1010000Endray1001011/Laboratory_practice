package ru.restaurant.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.restaurant.dto.MenuDto;
import ru.restaurant.model.menu.MenuItem;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class MenuService {
    private final Map<Integer, MenuItem> menuItems = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void loadMenu(String menuFilePath) {
        try {
            // Загрузка меню из JSON файла
        } catch (Exception e) {
            log.error("Error loading menu", e);
        }
    }

    public List<MenuItem> getActiveMenuItems() {
        return menuItems.values().stream()
                .filter(MenuItem::isActive)
                .toList();
    }

    public MenuItem getMenuItem(int id) {
        return menuItems.get(id);
    }
}