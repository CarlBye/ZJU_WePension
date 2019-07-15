package com.zjuwepension.application.service;

import com.zjuwepension.application.entity.CommodityOrder;

import java.util.List;

public interface CommodityOrderService {
    CommodityOrder saveComOrder(CommodityOrder commodityOrder);
    CommodityOrder updateComOrder(CommodityOrder commodityOrder);
    List<CommodityOrder> getAllOrderByUserId(Long userId);
    CommodityOrder findOrderByOrderIdAndUserId(Long userId, Long orderId);
    String changeOrderState(Long userId, Long orderId);
    Boolean hasUnFinishedOrder(Long tempId);
    CommodityOrder findTransportingOrderByTempId(Long tempId);
}
