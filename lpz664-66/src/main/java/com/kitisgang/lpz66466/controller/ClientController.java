package com.kitisgang.lpz66466.controller;

import com.kitisgang.lpz66466.entity.Client;
import com.kitisgang.lpz66466.exeption.ResourceNotFoundException;
import com.kitisgang.lpz66466.repository.ClientRepository;
import com.kitisgang.lpz66466.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final ClientRepository clientRepository;

    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getClientById(id));
    }

    @GetMapping("/by-email")
    public ResponseEntity<Client> getClientByEmail(@RequestParam String email) {
        return clientRepository.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Клиент не найден"));
    }

    @PostMapping
    public ResponseEntity<Client> createClient(@Valid @RequestBody Client client) {
        return new ResponseEntity<>(clientService.createClient(client), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @Valid @RequestBody Client client) {
        return ResponseEntity.ok(clientService.updateClient(id, client));
    }

    @PatchMapping("/{id}/bonus/add")
    public ResponseEntity<Client> addBonusPoints(
            @PathVariable Long id,
            @RequestParam int points) {
        return ResponseEntity.ok(clientService.addBonusPoints(id, points));
    }

    @PatchMapping("/{id}/bonus/use")
    public ResponseEntity<Client> useBonusPoints(
            @PathVariable Long id,
            @RequestParam int points) {
        return ResponseEntity.ok(clientService.useBonusPoints(id, points));
    }

    @GetMapping("/top")
    public ResponseEntity<List<Client>> getTopClients(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(clientService.getTopClients(limit));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Client>> searchClients(@RequestParam String query) {
        return ResponseEntity.ok(clientService.searchClients(query));
    }
}
