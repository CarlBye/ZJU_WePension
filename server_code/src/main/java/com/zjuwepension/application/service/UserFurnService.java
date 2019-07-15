package com.zjuwepension.application.service;

import com.zjuwepension.application.entity.Furniture;
import com.zjuwepension.application.entity.UserFurn;

import java.util.List;

public interface UserFurnService {
    UserFurn saveUserFurn(UserFurn userFurn);
    UserFurn updateUserFurn(UserFurn userFurn);
    List<UserFurn> findUserFurnsByUserId(Long userId);
    List<Furniture> findUnbindUserFurnsByUserId(Long userId);
}
