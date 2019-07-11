package com.zjuwepension.application.service.impl;

import com.zjuwepension.application.entity.CommodityOrderTemplate;
import com.zjuwepension.application.repository.CommodityOrderTemplateRepository;
import com.zjuwepension.application.service.CommodityOrderTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommodityOrderTemplateServiceImpl implements CommodityOrderTemplateService {
    @Autowired
    private CommodityOrderTemplateRepository commodityOrderTemplateRepository;

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public CommodityOrderTemplate saveTemplate(CommodityOrderTemplate template){
        return commodityOrderTemplateRepository.save(template);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public CommodityOrderTemplate updateTemplate(CommodityOrderTemplate template){
        return commodityOrderTemplateRepository.save(template);
    }

    @Override
    public CommodityOrderTemplate findActiveTemplateByButtonId(Long buttonId){
        List<CommodityOrderTemplate> list = commodityOrderTemplateRepository.findCommodityOrderTemplatesByButtonIdAndIsActive(buttonId, true);
        if (null != list && 1 == list.size())
            return list.get(0);
        return null;
    }

}
