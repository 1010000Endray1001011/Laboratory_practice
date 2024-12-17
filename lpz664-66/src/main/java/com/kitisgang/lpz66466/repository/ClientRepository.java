package com.kitisgang.lpz66466.repository;

import com.kitisgang.lpz66466.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    // Поиск клиента по email
    Optional<Client> findByEmail(String email);
    // Поиск клиента по номеру телефона
    Optional<Client> findByPhone(String phone);
    // Поиск клиентов по части ФИО
    List<Client> findByFullNameContainingIgnoreCase(String fullName);
    // Поиск клиентов с бонусным балансом больше указанного
    List<Client> findByBonusBalanceGreaterThan(Integer bonusBalance);
    // Поиск клиентов, зарегистрированных в определенный период
    List<Client> findByRegistrationDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    // Получение топ N клиентов по бонусному балансу
    @Query("SELECT c FROM Client c ORDER BY c.bonusBalance DESC LIMIT :limit")
    List<Client> findTopClientsByBonusBalance(@Param("limit") int limit);
    // Проверка существования клиента по email или телефону
    boolean existsByEmailOrPhone(String email, String phone);
    // Поиск клиентов без email
    List<Client> findByEmailIsNull();
}