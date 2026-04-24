package com.in5bv.rourb.service;

import com.in5bv.rourb.entity.Clients;

import java.util.List;

public interface ClientService {
    List<Clients> getAllClients();
    Clients saveClient(Clients client);
    Clients getClientById(Integer id);
    Clients updateClients(Integer id, Clients client);
    void deleteClients(Integer id);
}
