package com.in5bv.rourb.repository;

import com.in5bv.rourb.entity.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends JpaRepository<Sales, Integer> {
}
