package com.zjuwepension.application.repository;

import com.zjuwepension.application.entity.ButtonFurn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ButtonFurnRepository extends JpaRepository<ButtonFurn, Long> {
    List<ButtonFurn> findButtonFurnsByButtonIdAndIsActive(Long buttonId, Boolean isActive);
    List<ButtonFurn> findButtonFurnsByFurnIdAndIsActive(Long furnId, Boolean isActive);
}
