package com.zjuwepension.application.service;

import com.google.gson.JsonObject;
import com.zjuwepension.application.entity.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    User updateUser(User user);
    JsonObject verifyRegister(User user);
    User verifyLogIn(String name, String pwd);
    User findUserById(Long id);
    List<User> getAllUser();
}
