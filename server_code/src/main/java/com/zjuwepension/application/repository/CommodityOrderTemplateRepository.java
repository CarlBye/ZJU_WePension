package com.zjuwepension.application.repository;

import com.zjuwepension.application.entity.CommodityOrderTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommodityOrderTemplateRepository extends JpaRepository<CommodityOrderTemplate, Long> {
}
