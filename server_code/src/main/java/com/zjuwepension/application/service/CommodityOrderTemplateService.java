package com.zjuwepension.application.service;

import com.zjuwepension.application.entity.Commodity;
import com.zjuwepension.application.entity.CommodityOrderTemplate;

public interface CommodityOrderTemplateService {
    CommodityOrderTemplate saveTemplate(CommodityOrderTemplate template);
    CommodityOrderTemplate updateTemplate(CommodityOrderTemplate template);
    CommodityOrderTemplate findActiveTemplateByButtonId(Long buttonId);
    Commodity  findActiveCommodityByButtonId(Long buttonId);
}
