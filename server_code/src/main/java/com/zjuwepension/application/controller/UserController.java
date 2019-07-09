package com.zjuwepension.application.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zjuwepension.application.entity.User;
import com.zjuwepension.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String createUser(@RequestBody String body){
        JsonObject jsonData = new JsonParser().parse(body).getAsJsonObject();
        User regUser = new User();
        JsonObject result = null;
        if (jsonData.has("regName") && jsonData.has("regPwd") &&
                jsonData.has("regPhone") && jsonData.has("regEmail")) {
            // 根据post body初始化user实例
            regUser.setUserName(jsonData.get("regName").getAsString());
            // 加密 todo
            regUser.setUserPwd(jsonData.get("regPwd").getAsString());
            regUser.setUserPhoneNum(jsonData.get("regPhone").getAsString());
            regUser.setAlertPhoneNum(jsonData.get("regPhone").getAsString());
            regUser.setUserEmail(jsonData.get("regEmail").getAsString());
            Date currentTime = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            regUser.setDate(format.format(currentTime));
            regUser.setImgPath("");
            result = userService.verifyRegister(regUser);
            if(result.get("IsSuccess").getAsBoolean()){
                userService.saveUser(regUser);
            }
        } else {
            result = new JsonObject();
            result.addProperty("IsSuccess", false);
            result.addProperty("ErrorInfo", "参数不完整");
        }
        return result.toString();
    }

    @PostMapping("/login")
    public String logIn(@RequestBody String body){
        JsonObject jsonDate = new JsonParser().parse(body).getAsJsonObject();
        JsonObject result = new JsonObject();
        if (jsonDate.has("logName") && jsonDate.has("logPwd")) {
            User loginUser = userService.verifyLogIn(jsonDate.get("logName").getAsString(), jsonDate.get("logPwd").getAsString());
            if (null == loginUser){
                result.addProperty("IsSuccess", false);
                result.addProperty("ErrorInfo", "用户名或密码错误");
            } else {
                result.addProperty("IsSuccess", true);
                result.addProperty("curId", loginUser.getUserId().toString());
                result.addProperty("curName", loginUser.getUserName());
                result.addProperty("curPhone", loginUser.getUserPhoneNum());
                result.addProperty("curEmail", loginUser.getUserEmail());
                result.addProperty("curAlert", loginUser.getAlertPhoneNum());
                result.addProperty("curImgPath", loginUser.getImgPath());
                result.addProperty("ErrorInfo", "");
            }
        } else {
            result.addProperty("IsSuccess", false);
            result.addProperty("ErrorInfo", "参数不完整");
        }
        if (!result.get("IsSuccess").getAsBoolean()) {
            result.addProperty("curId", "");
            result.addProperty("curName", "");
            result.addProperty("curPhone", "");
            result.addProperty("curEmail", "");
            result.addProperty("curAlert", "");
            result.addProperty("curImgPath", "");
        }
        return result.toString();
    }

    @PostMapping("/update/name")
    public String updateUserName(@RequestBody String body){
        JsonObject jsonDate = new JsonParser().parse(body).getAsJsonObject();
        JsonObject result = new JsonObject();
        if (jsonDate.has("curId") && jsonDate.has("newName")) {
            User user = userService.findUserById(jsonDate.get("curId").getAsLong());
            user.setUserName(jsonDate.get("newName").getAsString());
            user = userService.updateUser(user);
            if (null != user){
                result.addProperty("IsSuccess", true);
                result.addProperty("curId", user.getUserId().toString());
                result.addProperty("curName", user.getUserName());
            } else {
                result.addProperty("IsSuccess", false);
            }
        } else {
            result.addProperty("IsSuccess", false);
        }
        if (!result.get("IsSuccess").getAsBoolean()){
            result.addProperty("curId", "");
            result.addProperty("curName", "");
        }
        return result.toString();
    }

    @PostMapping("/update/pwd")
    public String updateUserPwd(@RequestBody String body){
        JsonObject jsonDate = new JsonParser().parse(body).getAsJsonObject();
        JsonObject result = new JsonObject();
        if (jsonDate.has("curId") && jsonDate.has("newPwd") && jsonDate.has("oldPwd") ) {
            User user = userService.findUserById(jsonDate.get("curId").getAsLong());
            user.setUserPwd(jsonDate.get("newPwd").getAsString());
            user = userService.updateUser(user);
            if (null != user) {
                result.addProperty("IsSuccess", true);
                result.addProperty("curId", user.getUserId().toString());
            } else {
                result.addProperty("IsSuccess", false);
                result.addProperty("curId", "");
            }
        } else {
            result.addProperty("IsSuccess", false);
            result.addProperty("curId", "");
        }
        return result.toString();
    }

}
