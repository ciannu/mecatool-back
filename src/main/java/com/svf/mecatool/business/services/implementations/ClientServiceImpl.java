package com.svf.mecatool.business.services.implementations;

import com.svf.mecatool.business.services.ClientService;
import com.svf.mecatool.integration.model.Client;
import com.svf.mecatool.integration.model.Vehicle;
import com.svf.mecatool.integration.repositories.ClientRepository;
import com.svf.mecatool.integration.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final VehicleRepository vehicleRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, VehicleRepository vehicleRepository) {
        this.clientRepository = clientRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public Long createClient(Client client) {
        return clientRepository.save(client).getId();
    }

    @Override
    public Optional<Client> getClientById(Long id){
        return clientRepository.findById(id);
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public Client updateClient(Client client) {
        if (!clientRepository.existsById(client.getId())) {
            throw new RuntimeException("Client not found");
        }
        return clientRepository.save(client);
    }

    @Override
    public boolean deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            return false;
        }
        clientRepository.deleteById(id);
        return true;
    }

    @Override
    public List<Client> searchClients(String query) {
        return clientRepository.search(query);
    }

    @Override
    public List<Vehicle> getClientVehicles(Long clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new RuntimeException("Client not found");
        }
        return vehicleRepository.findByClientId(clientId);
    }

}
