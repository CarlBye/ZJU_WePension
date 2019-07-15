package com.zjuwepension.application.repository;

import com.zjuwepension.application.entity.Commodity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommodityRepository extends JpaRepository<Commodity, Long> {
    List<Commodity> findCommoditiesByComNo(String comNo);
    List<Commodity> findCommoditiesByComId(Long comId);
    List<Commodity> findCommoditiesByComNameContaining(String comName);
}
