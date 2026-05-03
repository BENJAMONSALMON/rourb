package com.in5bv.rourb.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Clients")
public class Clients {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_client")
    private Integer idClient;

    @Column(name = "client_dpi")
    private Integer clientDpi;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "client_last_name")
    private String clientLastName;

    @Column(name = "address")
    private String address;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private State state;

    public Integer getIdClient() {
        return idClient;
    }

    public void setIdClient(Integer idClient) {
        this.idClient = idClient;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Integer getClientDpi() {
        return clientDpi;
    }

    public void setClientDpi(Integer clientDpi) {
        this.clientDpi = clientDpi;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getClientLastName() {
        return clientLastName;
    }

    public void setClientLastName(String clientLastName) {
        this.clientLastName = clientLastName;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
