package com.zjuwepension.application.service.impl;

import com.google.gson.JsonObject;
import com.zjuwepension.application.entity.User;
import com.zjuwepension.application.repository.UserRepository;
import com.zjuwepension.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public User saveUser(User user){
        return userRepository.save(user);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public User updateUser(User user){
        return userRepository.save(user);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED, readOnly = true)
    @Override
    public JsonObject verifyRegister(User user){
        List<User> mailResult = userRepository.findUsersByUserEmail(user.getUserEmail());
        List<User> phoneResult = userRepository.findUsersByUserPhoneNum(user.getUserPhoneNum());
        JsonObject result = new JsonObject();
        if ((null == mailResult || 0 == mailResult.size()) && (null == phoneResult || 0 == phoneResult.size())){
            result.addProperty("IsSuccess", true);
            result.addProperty("ErrorInfo", "注册成功");
        } else {
            result.addProperty("IsSuccess", false);
            if (null != phoneResult && 0 < phoneResult.size()) {
                result.addProperty("ErrorInfo", "此手机号已被注册");
            } else if (null != mailResult && 0 < mailResult.size()){
                result.addProperty("ErrorInfo", "此邮箱已被注册");
            }
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED, readOnly = true)
    @Override
    public User verifyLogIn(String name, String pwd){
        List<User> resultList = userRepository.findUsersByUserNameAndUserPwd(name, pwd);
        if (null != resultList && 1 == resultList.size()){
            return resultList.get(0);
        } else {
            resultList = userRepository.findUsersByUserPhoneNumAndUserPwd(name, pwd);
            if (null != resultList && 1 == resultList.size()){
                return resultList.get(0);
            } else {
                resultList = userRepository.findUsersByUserEmailAndUserPwd(name, pwd);
                if (null != resultList && 1 == resultList.size()){
                    return resultList.get(0);
                } else {
                    return null;
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED, readOnly = true)
    @Override
    public User findUserById(Long id){
        List<User> result = userRepository.findUsersByUserId(id);
        if (null != result && 1 == result.size()){
            return result.get(0);
        } else {
            return null;
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public List<User> getAllUser(){
        return userRepository.findAll();
    }
}
