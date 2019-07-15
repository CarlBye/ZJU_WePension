package com.zjuwepension.application.service;

import com.zjuwepension.application.entity.Commodity;

import java.util.List;

public interface CommodityService {
    Commodity saveCommodity(Commodity com);
    Commodity updateCommodity(Commodity com);
    Commodity insertCommodity(Commodity commodity);
    Boolean hasCommodityOrderTemplate(Long buttonId, Long comId);
    List<Commodity> getAllComList();
    Commodity findCommodityByComId(Long comId);
    String updateCommodityByComId(Long comId, Long stack, Long price);
    String addOrderFromTemplateByButtonId(Long buttonId);
    List<Commodity> getComListContaningName(String comName);
    Commodity findComByTemplateId(Long templateId);
}
