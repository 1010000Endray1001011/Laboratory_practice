package ru.restaurant;

import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RestaurantApplication {
    public static void main(String[] args) {
        try {
            Runtime rt = Runtime.instance();
            Profile profile = new ProfileImpl();
            profile.setParameter(Profile.MAIN_HOST, "localhost");
            profile.setParameter(Profile.GUI, "true");

            AgentContainer mainContainer = rt.createMainContainer(profile);

            // Создаем агентов
            AgentController supervisor = mainContainer.createNewAgent(
                    "Руководитель",
                    "ru.restaurant.agents.SupervisorAgent",
                    null
            );
            supervisor.start();

            AgentController warehouse = mainContainer.createNewAgent(
                    "Склад",
                    "ru.restaurant.agents.WarehouseAgent",
                    null
            );
            warehouse.start();

            for (int i = 1; i <= 2; i++) {
                AgentController cook = mainContainer.createNewAgent(
                        "Повар" + i,
                        "ru.restaurant.agents.CookAgent",
                        null
                );
                cook.start();
            }

            AgentController equipment = mainContainer.createNewAgent(
                    "Оборудование1",
                    "ru.restaurant.agents.EquipmentAgent",
                    null
            );
            equipment.start();

            AgentController visitor = mainContainer.createNewAgent(
                    "Посетитель1",
                    "ru.restaurant.agents.VisitorAgent",
                    null
            );
            visitor.start();

            log.info("Успешно запущена система управления рестораном");

        } catch (Exception e) {
            log.error("Ошибка при запуске системы", e);
        }
    }
}