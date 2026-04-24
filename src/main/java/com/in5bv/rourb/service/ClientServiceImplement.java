package com.in5bv.rourb.service;

import com.in5bv.rourb.entity.Clients;
import com.in5bv.rourb.repository.ClientRepository;
import com.in5bv.rourb.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImplement implements ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceImplement(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public List<Clients> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public Clients saveClient(Clients client) {
        client.setIdClient(null);
        return clientRepository.save(client);
    }

    @Override
    public Clients getClientById(Integer id) {
        return clientRepository.findById(id).orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
    }

    @Override
    public Clients updateClients(Integer idUpdate, Clients client) {
        Clients existing = clientRepository.findById(idUpdate).orElseThrow(() -> new RuntimeException("Client not found with id: " + idUpdate));
        existing.setClientDpi(existing.getClientDpi());
        existing.setClientName(existing.getClientName());
        existing.setState(existing.getState());
        existing.setAdress(existing.getAdress());
        existing.setClientLastName(existing.getClientLastName());
        return clientRepository.save(existing);
    }

    @Override
    public void deleteClients(Integer idDelete) {
        Clients existing = getClientById((idDelete));
        clientRepository.delete(existing);

    }
}
