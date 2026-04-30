package com.in5bv.rourb.entity;


import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "SaleDetails")
public class SaleDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sale_detail")
    private Integer idSaleDetail;

    @Column(name = "detail_sale_code")
    private Integer detailSaleCode;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "unitary_price")
    private BigDecimal unitaryPrice;

    @Column(name = "subtotal")
    private BigDecimal subtotal;

    @ManyToOne
    @JoinColumn(name = "products_product_id", referencedColumnName = "id_product")
    private Products product;

    @ManyToOne
    @JoinColumn(name = "sales_sale_id", referencedColumnName = "id_sale")
    private Sales sale;

    public Integer getIdSaleDetail() {
        return idSaleDetail;
    }

    public void setIdSaleDetail(Integer idSaleDetail) {
        this.idSaleDetail = idSaleDetail;
    }

    public Integer getDetailSaleCode() {
        return detailSaleCode;
    }

    public void setDetailSaleCode(Integer detailSaleCode) {
        this.detailSaleCode = detailSaleCode;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getUnitaryPrice() {
        return unitaryPrice;
    }

    public void setUnitaryPrice(BigDecimal unitaryPrice) {
        this.unitaryPrice = unitaryPrice;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public Products getProduct() {
        return product;
    }

    public void setProduct(Products product) {
        this.product = product;
    }

    public Sales getSale() {
        return sale;
    }

    public void setSale(Sales sale) {
        this.sale = sale;
    }
}
