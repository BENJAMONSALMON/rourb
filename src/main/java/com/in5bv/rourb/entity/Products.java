package com.in5bv.rourb.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "Products")
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_product")
    private Integer idProduct;

    @Column(name = "product_code")
    private Integer productCode;

    @Column(name = "product_name")
    private Integer productName;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "stock")
    private Integer Stock;

    @Column(name = "state_product")
    @Enumerated(EnumType.STRING)
    private StateProduct stateProduct;

    public Integer getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Integer idProduct) {
        this.idProduct = idProduct;
    }

    public Integer getProductCode() {
        return productCode;
    }

    public void setProductCode(Integer productCode) {
        this.productCode = productCode;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getProductName() {
        return productName;
    }

    public void setProductName(Integer productName) {
        this.productName = productName;
    }

    public Integer getStock() {
        return Stock;
    }

    public void setStock(Integer stock) {
        Stock = stock;
    }

    public StateProduct getStateProduct() {
        return stateProduct;
    }

    public void setStateProduct(StateProduct stateProduct) {
        this.stateProduct = stateProduct;
    }
}