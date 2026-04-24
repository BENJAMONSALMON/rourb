package com.in5bv.rourb.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "Sales")
public class Sales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sale", nullable = false)
    private Integer idSale;

    @Column(name = "sale_code")
    private Integer saleCode;

    @Column(name = "sale_date")
    @Temporal(TemporalType.DATE)
    private Date saleDate;

    @Column(name = "total")
    private BigDecimal total;

    @Column(name = "state_sale")
    @Enumerated(EnumType.STRING)
    private StateSale stateSale;

    @ManyToOne
    @JoinColumn(name = "clients_client_id", referencedColumnName = "id_client")
    private Clients client;

    @ManyToOne
    @JoinColumn(name = "users_user_id", referencedColumnName = "id_user")
    private Users user;

    public Integer getIdSale() {
        return idSale;
    }

    public void setIdSale(Integer idSale) {
        this.idSale = idSale;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Clients getClient() {
        return client;
    }

    public void setClient(Clients client) {
        this.client = client;
    }

    public StateSale getStateSale() {
        return stateSale;
    }

    public void setStateSale(StateSale stateSale) {
        this.stateSale = stateSale;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public Integer getSaleCode() {
        return saleCode;
    }

    public void setSaleCode(Integer saleCode) {
        this.saleCode = saleCode;
    }
}