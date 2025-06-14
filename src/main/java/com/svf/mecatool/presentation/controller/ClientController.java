package com.svf.mecatool.presentation.controller;

import com.svf.mecatool.business.services.ClientService;
import com.svf.mecatool.integration.model.Client;
import com.svf.mecatool.integration.model.Vehicle;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.security.access.prepost.PreAuthorize;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Client client, UriComponentsBuilder ucb) {
        Long id = clientService.createClient(client);
        URI uri = ucb.path("/clients/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Client> client = clientService.getClientById(id);
        return client.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody Client client) {
        if (id == null || !id.equals(client.getId())) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Client updatedClient = clientService.updateClient(client);
            return ResponseEntity.ok(updatedClient);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }

        boolean deleted = clientService.deleteClient(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Client>> searchClients(@RequestParam String query) {
        List<Client> results = clientService.searchClients(query);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/{id}/vehicles")
    public ResponseEntity<List<Vehicle>> getClientVehicles(@PathVariable Long id) {
        try {
            List<Vehicle> vehicles = clientService.getClientVehicles(id);
            return ResponseEntity.ok(vehicles);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/total")
    @PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC')")
    public ResponseEntity<Long> getTotalClients() {
        long totalClients = clientService.getTotalClients();
        return ResponseEntity.ok(totalClients);
    }

    @GetMapping("/latest")
    @PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC')")
    public ResponseEntity<Client> getLatestClient() {
        Optional<Client> latestClient = clientService.getLatestClient();
        return latestClient.map(ResponseEntity::ok)
                           .orElseGet(() -> ResponseEntity.notFound().build());
    }
}