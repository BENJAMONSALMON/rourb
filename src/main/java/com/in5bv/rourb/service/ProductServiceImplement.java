package com.in5bv.rourb.service;

import com.in5bv.rourb.entity.Products;
import com.in5bv.rourb.entity.SaleDetails;
import com.in5bv.rourb.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImplement implements ProductService {

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
        return productRepository.save(product);
    }

    @Override
    public Products getProductById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    @Override
    public Products updateProducts(Integer idUpdate, SaleDetails saleDetails) {
        // this overload is kept for interface compatibility; use updateProduct(Products) instead
        Products existing = getProductById(idUpdate);
        return productRepository.save(existing);
    }

    public Products updateProduct(Products updated) {
        Products existing = getProductById(updated.getIdProduct());
        existing.setProductCode(updated.getProductCode());
        existing.setProductName(updated.getProductName());
        existing.setStateProduct(updated.getStateProduct());
        existing.setPrice(updated.getPrice());
        existing.setStock(updated.getStock());
        if (updated.getImageUrl() != null && !updated.getImageUrl().isBlank()) {
            existing.setImageUrl(updated.getImageUrl());
        }
        return productRepository.save(existing);
    }

    @Override
    public void deleteProducts(Integer idDelete) {
        Products existing = getProductById(idDelete);
        productRepository.delete(existing);
    }
}
