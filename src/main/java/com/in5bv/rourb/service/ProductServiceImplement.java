package com.in5bv.rourb.service;

import com.in5bv.rourb.entity.Products;
import com.in5bv.rourb.entity.SaleDetails;
import com.in5bv.rourb.repository.ProductRepository;
import com.in5bv.rourb.repository.SaleDetailRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductServiceImplement implements ProductService{

    private final ProductRepository productRepository;

    public ProductServiceImplement(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Override
    public List<Products> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Products saveProduct(Products product) {
        product.setIdProduct(null);
        return null;
    }

    @Override
    public Products getProductById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException
                        ("Product not found with id: " + id));
    }

    @Override
    public Products updateProducts(Integer idUpdate, SaleDetails saleDetails) {
        Products existing = productRepository.findById(idUpdate).orElseThrow(()-> new RuntimeException("Product not found with id: " + idUpdate));
        existing.setProductCode(existing.getProductCode());
        existing.setProductName(existing.getProductName());
        existing.setStateProduct(existing.getStateProduct());
        existing.setPrice(existing.getPrice());
        existing.setStock(existing.getStock());
        return productRepository.save(existing);

    }

    @Override
    public void deleteProducts(Integer idDelete) {
    Products existing = getProductById(idDelete);
    productRepository.delete(existing);
    }
}
