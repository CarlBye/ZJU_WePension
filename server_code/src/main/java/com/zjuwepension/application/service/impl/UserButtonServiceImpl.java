package com.zjuwepension.application.service.impl;

import com.zjuwepension.application.entity.UserButton;
import com.zjuwepension.application.repository.UserButtonRepository;
import com.zjuwepension.application.service.UserButtonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserButtonServiceImpl implements UserButtonService {
    @Autowired
    private UserButtonRepository userButtonRepository;

    @Override
    public UserButton saveUserButton(UserButton userButton){
        return userButtonRepository.save(userButton);
    }

    @Override
    public UserButton updateUserButton(UserButton userButton){
        return userButtonRepository.save(userButton);
    }
}
