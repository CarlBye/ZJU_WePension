package com.zjuwepension.application.repository;

import com.zjuwepension.application.entity.CommodityOrder;
import com.zjuwepension.application.entity.OrderStateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommodityOrderRepository extends JpaRepository<CommodityOrder, Long> {
    List<CommodityOrder> findCommodityOrdersByUserIdOrderByDateDesc(Long userId);
    List<CommodityOrder> findCommodityOrdersByUserIdAndOrderId(Long userId, Long orderId);
    List<CommodityOrder> findCommodityOrdersByTempIdAndOrderState(Long tempId, OrderStateType state);
}
