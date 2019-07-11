package com.zjuwepension.application.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zjuwepension.application.entity.*;
import com.zjuwepension.application.service.FurnitureService;
import com.zjuwepension.application.service.UserFurnService;
import com.zjuwepension.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/furniture")
public class FurnitureController {
    @Autowired
    private FurnitureService furnitureService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserFurnService userFurnService;

    @PostMapping("/bind")
    public String furnBind(@RequestBody String body){
        JsonObject jsonData = new JsonParser().parse(body).getAsJsonObject();
        JsonObject result = new JsonObject();
        if (jsonData.has("curId") && jsonData.has("furnId") && jsonData.has("furnName") && jsonData.has("furnType")) {
            User user = userService.findUserById(jsonData.get("curId").getAsLong());
            Furniture furniture = furnitureService.findFurnByFurnId(jsonData.get("furnId").getAsLong());
            if (null != user && null == furniture) {
                furniture = new Furniture();
                furniture.setFurnId(jsonData.get("furnId").getAsLong());
                furniture.setFurnName(jsonData.get("furnName").getAsString());
                furniture.setFurnType(FurnType.values()[jsonData.get("furnType").getAsInt()]);
                // todo get state from furniture
                furniture.setState(FurnStateType.OFF);
                furnitureService.saveFurn(furniture);
                UserFurn userFurn = new UserFurn();
                userFurn.setFurnId(furniture.getFurnId());
                userFurn.setUserId(user.getUserId());
                userFurnService.saveUserFurn(userFurn);
                result.addProperty("ErrorInfo", "");
            } else {
                if (null == user) {
                    result.addProperty("ErrorInfo", "非法用户");
                } else {
                    result.addProperty("ErrorInfo", "该家具已被绑定");
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
    public String getFurnList(@RequestBody String body){
        JsonObject jsonData = new JsonParser().parse(body).getAsJsonObject();
        JsonObject result = new JsonObject();
        if (jsonData.has("curId")) {
            User user = userService.findUserById(jsonData.get("curId").getAsLong());
            Furniture furniture = null;
            if (null != user) {
                result.addProperty("IsSuccess", true);
                result.addProperty("ErrorInfo", "");
                result.add("furnList", new JsonObject());
                List<UserFurn> list = userFurnService.findUserFurnsByUserId(user.getUserId());
                result.get("furnList").getAsJsonObject().addProperty("num", list.size());
                result.get("furnList").getAsJsonObject().add("list", new JsonArray());
                for (int i = 0; i < list.size(); i++) {
                    JsonObject furnElement = new JsonObject();
                    furniture = furnitureService.findFurnByFurnId(list.get(i).getFurnId());
                    furnElement.addProperty("furnId", furniture.getFurnId());
                    furnElement.addProperty("furnName", furniture.getFurnName());
                    furnElement.addProperty("furnType", furniture.getFurnType().ordinal());
                    result.get("furnList").getAsJsonObject().get("list").getAsJsonArray().add(furnElement);
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
