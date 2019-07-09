package com.zjuwepension.application.repository;

import com.zjuwepension.application.entity.UserButton;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserButtonRepository extends JpaRepository<UserButton, Long> {
}
