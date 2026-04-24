package com.in5bv.rourb.service;

import com.in5bv.rourb.entity.Sales;
import com.in5bv.rourb.repository.SaleRepository;
import com.in5bv.rourb.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SaleServiceImplement implements SaleService{

    private final SaleRepository saleRepository;

    public SaleServiceImplement(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    @Override
    public List<Sales> getAllSales() {
        return saleRepository.findAll();
    }

    @Override
    public Sales saveSales(Sales sale) {
        sale.setIdSale(null);
        return saleRepository.save(sale);
    }

    @Override
    public Sales getSaleById(Integer id) {
        return saleRepository.findById(id).orElseThrow(() -> new RuntimeException("Sale not found with id: " + id));
    }

    @Override
    public Sales updateSales(Integer idUpdate, Sales user) {
        Sales existing = saleRepository.findById(idUpdate).orElseThrow(() -> new RuntimeException("Sale not found with id: " + idUpdate));
        return null;
    }

    @Override
    public void deleteSales(Integer idDelete) {
        Sales existing = getSaleById(idDelete);
        saleRepository.delete(existing);
    }
}
