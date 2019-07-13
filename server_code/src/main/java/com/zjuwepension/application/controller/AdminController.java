package com.zjuwepension.application.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zjuwepension.application.entity.Admin;
import com.zjuwepension.application.entity.User;
import com.zjuwepension.application.service.AdminService;
import com.zjuwepension.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String adminRegister(@RequestParam(value = "regName") String regName,
                                @RequestParam(value = "regPwd") String regPwd){
        JsonObject result = new JsonObject();
        if (null != regName && !regName.equals("") && null != regPwd && !regPwd.equals("")) {
            Admin admin = new Admin();
            admin.setAdminName(regName);
            admin.setAdminPwd(regPwd);
            admin = adminService.registerAdmin(admin);
            if (null != admin) {
                result.addProperty("IsSuccess", true);
                result.addProperty("ErrorInfo", "");
            } else {
                result.addProperty("IsSuccess", false);
                result.addProperty("ErrorInfo", "注册失败");
            }
        } else {
            result.addProperty("IsSuccess", false);
            result.addProperty("ErrorInfo", "参数不完整");
        }
        return result.toString();
    }

    @PostMapping("/login")
    public String adminLogin(@RequestParam(value = "logName") String name,
                             @RequestParam(value = "logPwd") String pwd){
        JsonObject result = new JsonObject();
        if (null != name && !name.equals("") && null != pwd && !pwd.equals("")) {
            Admin admin = adminService.verifyAdminLogin(name, pwd);
            if (null != admin) {
                result.addProperty("IsSuccess", true);
                result.addProperty("ErrorInfo", "");
                result.addProperty("curId", admin.getAdminId().toString());
                result.addProperty("curName", admin.getAdminName());
            } else {
                result.addProperty("ErrorInfo", "登录失败");
            }
        } else {
            result.addProperty("ErrorInfo", "参数不完整");
        }
        if (!result.get("ErrorInfo").getAsString().equals("")){
            result.addProperty("IsSuccess", false);
            result.addProperty("curId", "");
            result.addProperty("curName", "");
        }

        return result.toString();
    }

    @PostMapping("/userList")
    public String getUserList(){
        JsonObject result = new JsonObject();
        List<User> list = userService.getAllUser();
        result.addProperty("num", new Long(list.size()).toString());
        result.add("userList", new JsonArray());
        for (int i = 0; i < list.size(); i++) {
            JsonObject userElement = new JsonObject();
            userElement.addProperty("userId", list.get(i).getUserId().toString());
            userElement.addProperty("userName", list.get(i).getUserName());
            userElement.addProperty("userPhone", list.get(i).getUserPhoneNum());
            userElement.addProperty("userEmail", list.get(i).getUserEmail());
            userElement.addProperty("userAlert", list.get(i).getAlertPhoneNum());
            result.get("userList").getAsJsonArray().add(userElement);
        }
        return result.toString();
    }
}
