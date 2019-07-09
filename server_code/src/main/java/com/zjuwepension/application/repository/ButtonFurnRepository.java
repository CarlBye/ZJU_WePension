package com.zjuwepension.application.repository;

import com.zjuwepension.application.entity.ButtonFurn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ButtonFurnRepository extends JpaRepository<ButtonFurn, Long> {

}
