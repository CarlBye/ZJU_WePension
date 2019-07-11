package com.zjuwepension.application.service.impl;

import com.zjuwepension.application.entity.Commodity;
import com.zjuwepension.application.entity.CommodityOrderTemplate;
import com.zjuwepension.application.repository.CommodityOrderTemplateRepository;
import com.zjuwepension.application.repository.CommodityRepository;
import com.zjuwepension.application.service.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommodityServiceImpl implements CommodityService {
    @Autowired
    private CommodityRepository commodityRepository;
    @Autowired
    private CommodityOrderTemplateRepository commodityOrderTemplateRepository;

    @Override
    public Commodity saveCommodity(Commodity com){
        return commodityRepository.save(com);
    }

    @Override
    public Commodity updateCommodity(Commodity com){
        return commodityRepository.save(com);
    }

    @Override
    public Boolean hasCommodityOrderTemplate(Long buttonId, Long comId){
        List<CommodityOrderTemplate> list = commodityOrderTemplateRepository.findCommodityOrderTemplatesByButtonIdAndIsActive(buttonId,true);
        if (null != list && 1 == list.size())
            return true;
        return false;
    }
}
