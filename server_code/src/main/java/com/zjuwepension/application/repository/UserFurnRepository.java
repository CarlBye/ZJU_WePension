package com.zjuwepension.application.repository;

import com.zjuwepension.application.entity.UserFurn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFurnRepository extends JpaRepository<UserFurn, Long> {
    List<UserFurn> findUserFurnsByUserId(Long userId);
}
