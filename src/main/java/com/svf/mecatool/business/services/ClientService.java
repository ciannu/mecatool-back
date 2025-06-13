package com.svf.mecatool.business.services;

import com.svf.mecatool.integration.model.Client;
import com.svf.mecatool.integration.model.Vehicle;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    Long createClient(Client client);
    Optional<Client> getClientById(Long id);
    List<Client> getAllClients();
    boolean deleteClient(Long id);
    Client updateClient(Client updatedClient);
    List<Client> searchClients(String query);
    List<Vehicle> getClientVehicles(Long clientId);
}
