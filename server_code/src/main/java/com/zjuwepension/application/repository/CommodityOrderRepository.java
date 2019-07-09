package com.zjuwepension.application.repository;

import com.zjuwepension.application.entity.CommodityOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommodityOrderRepository extends JpaRepository<CommodityOrder, Long> {
}
