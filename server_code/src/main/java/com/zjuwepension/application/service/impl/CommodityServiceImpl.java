package com.zjuwepension.application.service.impl;

import com.zjuwepension.application.entity.Commodity;
import com.zjuwepension.application.entity.CommodityOrderTemplate;
import com.zjuwepension.application.repository.CommodityOrderTemplateRepository;
import com.zjuwepension.application.repository.CommodityRepository;
import com.zjuwepension.application.service.CommodityService;
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

}
