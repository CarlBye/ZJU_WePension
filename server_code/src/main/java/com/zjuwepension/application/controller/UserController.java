package com.zjuwepension.application.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zjuwepension.application.entity.User;
import com.zjuwepension.application.service.UserService;
import com.zjuwepension.tool.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Value("${server.port}")
    private String port;

    @PostMapping("/register")
    public String createUser(@RequestBody String body){
        JsonObject jsonData = new JsonParser().parse(body).getAsJsonObject();
        User regUser = new User();
        JsonObject result = null;
        if (jsonData.has("regName") && jsonData.has("regPwd") &&
                jsonData.has("regPhone") && jsonData.has("regEmail") &&
                jsonData.has("regDescription") && jsonData.has("regFaceId")) {
            // 根据post body初始化user实例
            regUser.setUserName(jsonData.get("regName").getAsString());
            // 加密 todo
            regUser.setUserPwd(jsonData.get("regPwd").getAsString());
            regUser.setUserPhoneNum(jsonData.get("regPhone").getAsString());
            regUser.setAlertPhoneNum(jsonData.get("regPhone").getAsString());
            regUser.setUserEmail(jsonData.get("regEmail").getAsString());
            regUser.setDescription(jsonData.get("regDescription").getAsString());
            regUser.setDate(Tool.getDate());
            regUser.setFaceId(jsonData.get("regFaceId").getAsShort());
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
                result.addProperty("curDescription", loginUser.getDescription());
                result.addProperty("curFaceId", loginUser.getFaceId().toString());
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
            result.addProperty("curDescription", "");
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

    @PostMapping("/update/description")
    public String updateUserDescription(@RequestBody String body){
        JsonObject jsonDate = new JsonParser().parse(body).getAsJsonObject();
        JsonObject result = new JsonObject();
        if (jsonDate.has("curId") && jsonDate.has("newDescription")) {
            User user = userService.findUserById(jsonDate.get("curId").getAsLong());
            user.setDescription(jsonDate.get("newDescription").getAsString());
            user = userService.updateUser(user);
            if (null != user){
                result.addProperty("IsSuccess", true);
                result.addProperty("curId", user.getUserId().toString());
                result.addProperty("curDescription", user.getDescription());
            } else {
                result.addProperty("IsSuccess", false);
            }
        } else {
            result.addProperty("IsSuccess", false);
        }
        if (!result.get("IsSuccess").getAsBoolean()){
            result.addProperty("curId", "");
            result.addProperty("curDescription", "");
        }
        return result.toString();
    }

    /*
    @PostMapping("/register/testForm")
    public String registerForm(@RequestParam(value = "regName") String regName,
                               @RequestParam(value = "regPwd") String regPwd,
                               @RequestParam(value = "regPhone") String regPhone,
                               @RequestParam(value = "regEmail") String regEmail,
                               @RequestParam(value = "regDescription") String regDescription,
                               @RequestParam(value = "regImg") MultipartFile regImg){
        User regUser = new User();
        JsonObject result = null;
        if (null != regName && null != regPwd && null != regPhone && null != regEmail && null != regDescription && !regImg.isEmpty()) {
            regUser.setUserName(regName);
            regUser.setUserPwd(regPwd);
            regUser.setUserPhoneNum(regPhone);
            regUser.setAlertPhoneNum(regPhone);
            regUser.setUserEmail(regEmail);
            regUser.setDescription(regDescription);
            regUser.setImgPath("");
            Date currentTime = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            regUser.setDate(format.format(currentTime));
            try{
                byte[] bytes = regImg.getBytes();
                String fileName = regImg.getOriginalFilename();
                String suffixName = fileName.substring(fileName.lastIndexOf("."));
                fileName = UUID.randomUUID().toString().replace("-", "") + suffixName;
                regUser.setImgPath("http://47.100.98.181:" + port + "/user/userImg/" + fileName);
                String pathName = System.getProperty("user.dir");
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(pathName + "/static/user/userImg" + fileName)));
                bos.write(bytes);
                bos.close();
            } catch (Exception e){
                e.printStackTrace();
            }
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
    }*/

}
