package com.in5bv.rourb.service;

import com.in5bv.rourb.entity.SaleDetails;
import com.in5bv.rourb.repository.SaleDetailRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleDetailServiceImplement implements SaleDetailService {

    private final SaleDetailRepository saleDetailRepository;

    public SaleDetailServiceImplement(SaleDetailRepository saleDetailRepository) {
        this.saleDetailRepository = saleDetailRepository;
    }

    @Override
    public List<SaleDetails> getAllSaleDetails() {
        return saleDetailRepository.findAll();
    }

    @Override
    public SaleDetails saveSaleDetails(SaleDetails saleDetail) {
        saleDetail.setIdSaleDetail(null);
        return saleDetailRepository.save(saleDetail);
    }

    @Override
    public SaleDetails getSaleDetailById(Integer id) {
        return saleDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale Detail not found with id: " + id));
    }

    @Override
    public SaleDetails updateSaleDetails(Integer idUpdate, SaleDetails saleDetails) {
        SaleDetails existing = getSaleDetailById(idUpdate);
        existing.setSale(saleDetails.getSale());
        existing.setDetailSaleCode(saleDetails.getDetailSaleCode());
        existing.setAmount(saleDetails.getAmount());
        existing.setProduct(saleDetails.getProduct());
        existing.setSubtotal(saleDetails.getSubtotal());
        existing.setUnitaryPrice(saleDetails.getUnitaryPrice());
        return saleDetailRepository.save(existing);
    }

    @Override
    public void deleteSaleDetails(Integer idDelete) {
        SaleDetails existing = getSaleDetailById(idDelete);
        saleDetailRepository.delete(existing);
    }
}
