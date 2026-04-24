package com.in5bv.rourb.service;

import com.in5bv.rourb.entity.Products;
import com.in5bv.rourb.entity.SaleDetails;

import java.util.List;

public interface ProductService {
    List<Products> getAllProducts();
    Products saveProduct(Products product);
    Products getProductById(Integer id);
    Products updateProducts(Integer id, SaleDetails saleDetails);
    void deleteProducts(Integer id);
}
