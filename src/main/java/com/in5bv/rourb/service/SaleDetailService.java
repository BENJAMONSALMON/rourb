package com.in5bv.rourb.service;

import com.in5bv.rourb.entity.SaleDetails;
import com.in5bv.rourb.entity.Sales;
import com.in5bv.rourb.entity.Users;

import java.util.List;

public interface SaleDetailService {
    List<SaleDetails> getAllSaleDetails();
    SaleDetails saveSaleDetails(SaleDetails saleDetail);
    SaleDetails getSaleDetailById(Integer id);
    SaleDetails updateSaleDetails(Integer id, SaleDetails saleDetails);
    void deleteSaleDetails(Integer id);
}
