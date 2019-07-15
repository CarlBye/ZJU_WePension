package com.zjuwepension.application.service.impl;

import com.zjuwepension.application.entity.CommodityOrder;
import com.zjuwepension.application.entity.OrderStateType;
import com.zjuwepension.application.repository.CommodityOrderRepository;
import com.zjuwepension.application.service.CommodityOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommodityOrderServiceImpl implements CommodityOrderService {
    @Autowired
    private CommodityOrderRepository commodityOrderRepository;

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public CommodityOrder saveComOrder(CommodityOrder commodityOrder){
        return commodityOrderRepository.save(commodityOrder);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public CommodityOrder updateComOrder(CommodityOrder commodityOrder){
        return commodityOrderRepository.save(commodityOrder);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public List<CommodityOrder> getAllOrderByUserId(Long userId){
        return commodityOrderRepository.findCommodityOrdersByUserIdOrderByDateDesc(userId);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public CommodityOrder findOrderByOrderIdAndUserId(Long userId, Long orderId){
        List<CommodityOrder> list = commodityOrderRepository.findCommodityOrdersByUserIdAndOrderId(userId, orderId);
        if (null != list && 1 == list.size())
            return list.get(0);
        return null;
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public String changeOrderState(Long userId, Long orderId){
        CommodityOrder order = findOrderByOrderIdAndUserId(userId, orderId);
        if (null == order)
            return "非法订单";
        switch (order.getOrderState()){
            case UNKNOWN:
            case COMPLETING:
            case TRANSPORTING:
                break;
            case TOBECONFIRMED:
                order.setOrderState(OrderStateType.TRANSPORTING);
                break;
            case SIGNING:
                order.setOrderState(OrderStateType.COMPLETING);
                break;
        }
        return "";
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public Boolean hasUnFinishedOrder(Long tempId){
        List<CommodityOrder> list = commodityOrderRepository.findCommodityOrdersByTempIdAndOrderState(tempId, OrderStateType.TOBECONFIRMED);
        if (null != list && 0 < list.size())
            return true;
        list = commodityOrderRepository.findCommodityOrdersByTempIdAndOrderState(tempId, OrderStateType.TRANSPORTING);
        if (null != list && 0 < list.size())
            return true;
        list = commodityOrderRepository.findCommodityOrdersByTempIdAndOrderState(tempId, OrderStateType.SIGNING);
        if (null != list && 0 < list.size())
            return true;
        return false;
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public CommodityOrder findTransportingOrderByTempId(Long tempId){
        List<CommodityOrder> list = commodityOrderRepository.findCommodityOrdersByTempIdAndOrderState(tempId, OrderStateType.TRANSPORTING);
        if (null != list && 1 == list.size()){
            return list.get(0);
        } else {
            return null;
        }
    }

}
