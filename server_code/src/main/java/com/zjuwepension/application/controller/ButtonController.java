package com.zjuwepension.application.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zjuwepension.application.entity.Button;
import com.zjuwepension.application.entity.ButtonType;
import com.zjuwepension.application.entity.User;
import com.zjuwepension.application.entity.UserButton;
import com.zjuwepension.application.service.ButtonService;
import com.zjuwepension.application.service.UserButtonService;
import com.zjuwepension.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/button")
public class ButtonController {
    @Autowired
    private UserService userService;
    @Autowired
    private ButtonService buttonService;
    @Autowired
    private UserButtonService userButtonService;

    @PostMapping("/bind")
    public String buttonBind(@RequestBody String body){
        JsonObject jsonData = new JsonParser().parse(body).getAsJsonObject();
        JsonObject result = new JsonObject();
        if (jsonData.has("curId") && jsonData.has("buttonId") && jsonData.has("buttonName")) {
            User user = userService.findUserById(jsonData.get("curId").getAsLong());
            Button button = buttonService.findButtonById(jsonData.get("buttonId").getAsLong());
            if (null != user && null == button) {
                button = new Button();
                button.setButtonId(jsonData.get("buttonId").getAsLong());
                button.setButtonName(jsonData.get("buttonName").getAsString());
                button.setButtonType(ButtonType.UNKNOW);
                buttonService.saveButton(button);
                UserButton userButton = new UserButton();
                userButton.setButtonId(button.getButtonId());
                userButton.setUserId(user.getUserId());
                userButtonService.saveUserButton(userButton);
                result.addProperty("ErrorInfo", "");
            } else {
                if (null == user) {
                    result.addProperty("ErrorInfo", "非法用户");
                } else {
                    result.addProperty("ErrorInfo", "该按钮已被绑定");
                }
            }
        } else {
            result.addProperty("ErrorInfo", "参数不完整");
        }
        if (result.get("ErrorInfo").getAsString().equals("")) {
            result.addProperty("IsSuccess", true);
        } else {
            result.addProperty("IsSuccess", false);
        }
        return result.toString();
    }

    @PostMapping("/list")
    public String getButtonList(@RequestBody String body){
        JsonObject jsonData = new JsonParser().parse(body).getAsJsonObject();
        JsonObject result = new JsonObject();
        if (jsonData.has("curId")) {
            User user = userService.findUserById(jsonData.get("curId").getAsLong());
            Button tempButton = null;
            if (null != user) {
                result.addProperty("IsSuccess", true);
                result.addProperty("ErrorInfo", "");
                result.add("buttonList", new JsonObject());
                List<UserButton> list = userButtonService.findUserButtonsByUserId(user.getUserId());
                result.get("buttonList").getAsJsonObject().addProperty("num", list.size());
                result.get("buttonList").getAsJsonObject().add("list", new JsonArray());
                for (int i = 0; i < list.size(); i++) {
                    JsonObject buttonElement = new JsonObject();
                    tempButton = buttonService.findButtonById(list.get(i).getButtonId());
                    buttonElement.addProperty("buttonId", tempButton.getButtonId());
                    buttonElement.addProperty("buttonName", tempButton.getButtonName());
                    buttonElement.addProperty("buttonType", tempButton.getButtonType().ordinal());
                    result.get("buttonList").getAsJsonObject().get("list").getAsJsonArray().add(buttonElement);
                }
            } else {
                result.addProperty("IsSuccess", false);
                result.addProperty("ErrorInfo", "非法用户");
                result.addProperty("buttonList", "");
            }
        } else {
            result.addProperty("IsSuccess", false);
            result.addProperty("ErrorInfo", "参数不完整");
            result.addProperty("buttonList", "");
        }
        return result.toString();
    }
}
