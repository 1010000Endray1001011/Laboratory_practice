package com.kitisgang.lpz66466.service;

import com.kitisgang.lpz66466.entity.Client;
import com.kitisgang.lpz66466.repository.ClientRepository;
import com.kitisgang.lpz66466.exeption.ResourceNotFoundException;
import com.kitisgang.lpz66466.exeption.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClientService {

    private final ClientRepository clientRepository;

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Клиент не найден с ID: " + id));
    }
    public Optional<Client> findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }


    @Transactional
    public Client createClient(Client client) {
        if (clientRepository.existsByEmailOrPhone(client.getEmail(), client.getPhone())) {
            throw new BusinessException("Клиент с таким email или телефоном уже существует");
        }
        return clientRepository.save(client);
    }

    @Transactional
    public Client updateClient(Long id, Client clientDetails) {
        Client client = getClientById(id);

        if (!client.getEmail().equals(clientDetails.getEmail()) &&
                !client.getPhone().equals(clientDetails.getPhone()) &&
                clientRepository.existsByEmailOrPhone(clientDetails.getEmail(), clientDetails.getPhone())) {
            throw new BusinessException("Клиент с таким email или телефоном уже существует");
        }

        client.setFullName(clientDetails.getFullName());
        client.setEmail(clientDetails.getEmail());
        client.setPhone(clientDetails.getPhone());

        return clientRepository.save(client);
    }

    @Transactional
    public Client addBonusPoints(Long id, int points) {
        Client client = getClientById(id);
        client.setBonusBalance(client.getBonusBalance() + points);
        return clientRepository.save(client);
    }

    @Transactional
    public Client useBonusPoints(Long id, int points) {
        Client client = getClientById(id);
        if (client.getBonusBalance() < points) {
            throw new BusinessException("Недостаточно бонусных баллов");
        }
        client.setBonusBalance(client.getBonusBalance() - points);
        return clientRepository.save(client);
    }

    public List<Client> getTopClients(int limit) {
        return clientRepository.findTopClientsByBonusBalance(limit);
    }

    public List<Client> searchClients(String searchTerm) {
        return clientRepository.findByFullNameContainingIgnoreCase(searchTerm);
    }
}
