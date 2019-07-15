package com.zjuwepension.application.service.impl;

import com.zjuwepension.application.entity.Furniture;
import com.zjuwepension.application.entity.UserFurn;
import com.zjuwepension.application.repository.UserFurnRepository;
import com.zjuwepension.application.service.ButtonFurnService;
import com.zjuwepension.application.service.FurnitureService;
import com.zjuwepension.application.service.UserFurnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserFurnServiceImpl implements UserFurnService {
    @Autowired
    private UserFurnRepository userFurnRepository;
    @Autowired
    private FurnitureService furnitureService;
    @Autowired
    private ButtonFurnService buttonFurnService;
    @Override
    public UserFurn saveUserFurn(UserFurn userFurn){
        return userFurnRepository.save(userFurn);
    }

    @Override
    public UserFurn updateUserFurn(UserFurn userFurn){
        return userFurnRepository.save(userFurn);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public List<UserFurn> findUserFurnsByUserId(Long userId){
        return userFurnRepository.findUserFurnsByUserId(userId);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public List<Furniture> findUnbindUserFurnsByUserId(Long userId){
        List<UserFurn> list = findUserFurnsByUserId(userId);
        if (null == list)
            return null;
        List<Furniture> furnList = new ArrayList<Furniture>();
        for (int i = 0; i < list.size(); i++) {
            if(null == buttonFurnService.findActiveButtonFurnByFurnId(list.get(i).getFurnId())) {
                furnList.add(furnitureService.findFurnByFurnId(list.get(i).getFurnId()));
            }
        }
        return furnList;
    }
}
