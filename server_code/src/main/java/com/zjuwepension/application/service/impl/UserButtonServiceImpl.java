package com.zjuwepension.application.service.impl;

import com.zjuwepension.application.entity.UserButton;
import com.zjuwepension.application.repository.UserButtonRepository;
import com.zjuwepension.application.service.UserButtonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Override
    public List<UserButton> findUserButtonsByUserId(Long userId){
        return userButtonRepository.findUserButtonsByUserId(userId);
    }

    @Override
    public Boolean hasUserButton(Long userId, Long buttonId){
        List<UserButton> list = userButtonRepository.findUserButtonsByUserIdAndAndButtonId(userId, buttonId);
        if (null != list && 1 == list.size()) {
            return true;
        } else {
            return false;
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public UserButton findUserButtonByButtonId(Long buttonId){
        List<UserButton> list = userButtonRepository.findUserButtonsByButtonId(buttonId);
        if (null != list && 1 == list.size())
            return list.get(0);
        return null;
    }
}
