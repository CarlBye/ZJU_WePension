package com.zjuwepension.application.repository;

import com.zjuwepension.application.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    List<Admin> findAdminsByAdminName(String name);
    List<Admin> findAdminsByAdminNameAndAdminPwd(String name, String pwd);
}
