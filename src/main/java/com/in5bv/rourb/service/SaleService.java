package com.in5bv.rourb.service;

import com.in5bv.rourb.entity.Sales;

import java.util.List;

public interface SaleService {
    List<Sales> getAllSales();
    Sales saveSales(Sales user);
    Sales getSaleById(Integer id);
    Sales updateSales(Integer id, Sales user);
    void deleteSales(Integer id);
}
