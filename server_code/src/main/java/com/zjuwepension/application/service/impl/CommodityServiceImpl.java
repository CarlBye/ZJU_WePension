package com.zjuwepension.application.service.impl;

import com.zjuwepension.application.entity.*;
import com.zjuwepension.application.repository.CommodityOrderTemplateRepository;
import com.zjuwepension.application.repository.CommodityRepository;
import com.zjuwepension.application.service.CommodityOrderService;
import com.zjuwepension.application.service.CommodityOrderTemplateService;
import com.zjuwepension.application.service.CommodityService;
import com.zjuwepension.application.service.UserButtonService;
import com.zjuwepension.tool.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommodityServiceImpl implements CommodityService {
    @Autowired
    private CommodityRepository commodityRepository;
    @Autowired
    private CommodityOrderTemplateRepository commodityOrderTemplateRepository;
    @Autowired
    private CommodityOrderTemplateService commodityOrderTemplateService;
    @Autowired
    private CommodityOrderService commodityOrderService;
    @Autowired
    private UserButtonService userButtonService;

    @Override
    public Commodity saveCommodity(Commodity com){
        return commodityRepository.save(com);
    }

    @Override
    public Commodity updateCommodity(Commodity com){
        return commodityRepository.save(com);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public Boolean hasCommodityOrderTemplate(Long buttonId, Long comId){
        List<CommodityOrderTemplate> list = commodityOrderTemplateRepository.findCommodityOrderTemplatesByButtonIdAndIsActive(buttonId,true);
        if (null != list && 1 == list.size())
            return true;
        return false;
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public Commodity insertCommodity(Commodity commodity){
        List<Commodity> list = commodityRepository.findCommoditiesByComNo(commodity.getComNo());
        if (null != list && 0 < list.size()) {
            return null;
        }
        commodity = commodityRepository.save(commodity);
        return commodity;
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public List<Commodity> getAllComList(){
        return commodityRepository.findAll();
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public Commodity findCommodityByComId(Long comId){
        List<Commodity> list = commodityRepository.findCommoditiesByComId(comId);
        if(null != list && 1 == list.size())
            return list.get(0);
        return null;
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public String updateCommodityByComId(Long comId, Long stack, Long price){
        Commodity commodity = findCommodityByComId(comId);
        if (null == commodity)
            return "不存在该商品";
        commodity.setStack(stack);
        commodity.setPrice(price);
        commodity = commodityRepository.save(commodity);
        if (null == commodity)
            return "更新失败";
        return "";
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public String addOrderFromTemplateByButtonId(Long buttonId){
        CommodityOrderTemplate template = commodityOrderTemplateService.findActiveTemplateByButtonId(buttonId);
        if (null == template)
            return "订单模板不存在";
        if (commodityOrderService.hasUnFinishedOrder(template.getTempId())){
            CommodityOrder order = commodityOrderService.findTransportingOrderByTempId(template.getTempId());
            if (null == order) {
                return "按钮无效操作阶段";
            } else {
                order.setOrderState(OrderStateType.SIGNING);
                order = commodityOrderService.updateComOrder(order);
                if (null == order) {
                    return "按钮状态更新失败";
                }
                return "";
            }
        } else {
            Commodity commodity = findCommodityByComId(template.getComId());
            if (null == commodity)
                return "商品不存在";
            if (commodity.getStack() >= template.getNum()){
                commodity.setStack(commodity.getStack() - template.getNum());
                CommodityOrder commodityOrder = new CommodityOrder();
                commodityOrder.setDate(Tool.getDate());
                commodityOrder.setTempId(template.getTempId());
                commodityOrder.setOrderState(OrderStateType.TOBECONFIRMED);
                UserButton userButton = userButtonService.findUserButtonByButtonId(buttonId);
                if (null == userButton)
                    return "该按钮未绑定用户";
                commodityOrder.setUserId(userButton.getUserId());
                saveCommodity(commodity);
                commodityOrderService.saveComOrder(commodityOrder);
                return "";
            } else {
                return "余量不足";
            }
        }

    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public List<Commodity> getComListContaningName(String comName){
        return commodityRepository.findCommoditiesByComNameContaining(comName);
    }


    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public Commodity findComByTemplateId(Long templateId){
        CommodityOrderTemplate template = commodityOrderTemplateService.findTemplateByTemplateId(templateId);
        if (null == template)
            return null;
        return findCommodityByComId(template.getComId());
    }
}
