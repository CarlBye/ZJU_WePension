package com.zjuwepension.application.repository;

import com.zjuwepension.application.entity.CommodityOrderTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommodityOrderTemplateRepository extends JpaRepository<CommodityOrderTemplate, Long> {
    List<CommodityOrderTemplate> findCommodityOrderTemplatesByButtonIdAndIsActive(Long buttonId, Boolean isActive);
    List<CommodityOrderTemplate> findCommodityOrderTemplatesByTempId(Long tempId);
}
