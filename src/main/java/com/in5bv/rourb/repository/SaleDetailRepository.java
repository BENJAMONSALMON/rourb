package com.in5bv.rourb.repository;

import com.in5bv.rourb.entity.SaleDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleDetailRepository extends JpaRepository<SaleDetails, Integer> {
}
