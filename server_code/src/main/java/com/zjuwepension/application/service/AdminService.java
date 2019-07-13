package com.zjuwepension.application.service;

import com.zjuwepension.application.entity.Admin;

public interface AdminService {
    Admin saveAdmin(Admin admin);
    Admin updateAdmin(Admin admin);
    Admin registerAdmin(Admin admin);
    Admin verifyAdminLogin(String name, String pwd);
}
