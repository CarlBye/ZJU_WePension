package com.zjuwepension.application.service.impl;

import com.zjuwepension.application.entity.UserFurn;
import com.zjuwepension.application.repository.UserFurnRepository;
import com.zjuwepension.application.service.UserFurnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserFurnServiceImpl implements UserFurnService {
    @Autowired
    private UserFurnRepository userFurnRepository;

    @Override
    public UserFurn saveUserFurn(UserFurn userFurn){
        return userFurnRepository.save(userFurn);
    }

    @Override
    public UserFurn updateUserFurn(UserFurn userFurn){
        return userFurnRepository.save(userFurn);
    }

    @Override
    public List<UserFurn> findUserFurnsByUserId(Long userId){
        return userFurnRepository.findUserFurnsByUserId(userId);
    }
}
