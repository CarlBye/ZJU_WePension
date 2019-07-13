package com.zjuwepension.application.service.impl;

import com.zjuwepension.application.entity.Admin;
import com.zjuwepension.application.repository.AdminRepository;
import com.zjuwepension.application.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminRepository adminRepository;

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public Admin saveAdmin(Admin admin){
        return adminRepository.save(admin);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public Admin updateAdmin(Admin admin){
        return adminRepository.save(admin);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public Admin registerAdmin(Admin admin){
        List<Admin> list = adminRepository.findAdminsByAdminName(admin.getAdminName());
        if (null != list && 0 < list.size())
            return null;
        return adminRepository.save(admin);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public Admin verifyAdminLogin(String name, String pwd){
        List<Admin> list = adminRepository.findAdminsByAdminNameAndAdminPwd(name, pwd);
        if (null != list && 1 == list.size())
            return list.get(0);
        return null;
    }
}
