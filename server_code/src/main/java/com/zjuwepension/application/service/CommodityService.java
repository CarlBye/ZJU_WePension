package com.zjuwepension.application.service;

import com.zjuwepension.application.entity.Commodity;

public interface CommodityService {
    Commodity saveCommodity(Commodity com);
    Commodity updateCommodity(Commodity com);
    Boolean hasCommodityOrderTemplate(Long buttonId, Long comId);
}
