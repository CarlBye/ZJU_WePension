package com.zjuwepension.application.service;

import com.zjuwepension.application.entity.UserButton;

import java.util.List;

public interface UserButtonService {
    UserButton saveUserButton(UserButton userButton);
    UserButton updateUserButton(UserButton userButton);
    List<UserButton> findUserButtonsByUserId(Long userId);
}
