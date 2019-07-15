package com.zjuwepension.application.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zjuwepension.application.entity.*;
import com.zjuwepension.application.service.*;
import com.zjuwepension.tool.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/button")
public class ButtonController {
    @Autowired
    private UserService userService;
    @Autowired
    private ButtonService buttonService;
    @Autowired
    private UserButtonService userButtonService;
    @Autowired
    private FurnitureService furnitureService;
    @Autowired
    private CommodityOrderTemplateService commodityOrderTemplateService;
    @Autowired
    private CommodityService commodityService;

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
                result.get("buttonList").getAsJsonObject().addProperty("num", new Integer(list.size()).toString());
                result.get("buttonList").getAsJsonObject().add("list", new JsonArray());
                for (int i = 0; i < list.size(); i++) {
                    JsonObject buttonElement = new JsonObject();
                    tempButton = buttonService.findButtonById(list.get(i).getButtonId());
                    buttonElement.addProperty("buttonId", tempButton.getButtonId().toString());
                    buttonElement.addProperty("buttonName", tempButton.getButtonName());
                    buttonElement.addProperty("buttonType", new Long(tempButton.getButtonType().ordinal()).toString());
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

    @PostMapping("/bind/furniture")
    public String buttonBindFurn(@RequestBody String body){
        JsonObject jsonData = new JsonParser().parse(body).getAsJsonObject();
        JsonObject result = new JsonObject();
        if (jsonData.has("curId") && jsonData.has("buttonId") && jsonData.has("furnId")) {
            if (userButtonService.hasUserButton(jsonData.get("curId").getAsLong(), jsonData.get("buttonId").getAsLong())) {
                Button button = buttonService.findButtonById(jsonData.get("buttonId").getAsLong());
                button = buttonService.updateButtonFurn(button, jsonData.get("furnId").getAsLong());
                if (null != button) {
                    result.addProperty("ErrorInfo", "");
                } else {
                    result.addProperty("ErrorInfo", "绑定失败");
                }
            } else {
                result.addProperty("ErrorInfo", "该用户未绑定该按钮");
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

    @PostMapping("/bind/alert")
    public String buttonBindAlert(@RequestBody String body){
        JsonObject jsonData = new JsonParser().parse(body).getAsJsonObject();
        JsonObject result = new JsonObject();
        if (jsonData.has("curId") && jsonData.has("buttonId")) {
            if (userButtonService.hasUserButton(jsonData.get("curId").getAsLong(), jsonData.get("buttonId").getAsLong())) {
                Button button = buttonService.findButtonById(jsonData.get("buttonId").getAsLong());
                button = buttonService.updateButtonAlert(button);
                if (null != button) {
                    result.addProperty("ErrorInfo", "");
                } else {
                    result.addProperty("ErrorInfo", "绑定失败");
                }
            } else {
                result.addProperty("ErrorInfo", "该用户未绑定该按钮");
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

    @PostMapping("/bind/commodity")
    public String buttonBindCommodity(@RequestBody String body){
        JsonObject jsonData = new JsonParser().parse(body).getAsJsonObject();
        JsonObject result = new JsonObject();
        if (jsonData.has("curId") && jsonData.has("comId") &&
            jsonData.has("buttonId") && jsonData.has("num") &&
            jsonData.has("comDeliveryAddress") && jsonData.has("comDeliveryPhone") &&
            jsonData.has("comDeliveryName")) {
            if (userButtonService.hasUserButton(jsonData.get("curId").getAsLong(), jsonData.get("buttonId").getAsLong())){
                Button button = buttonService.findButtonById(jsonData.get("buttonId").getAsLong());
                CommodityOrderTemplate template = new CommodityOrderTemplate();
                template.setNum(jsonData.get("num").getAsLong());
                template.setComId(jsonData.get("comId").getAsLong());
                template.setDeliveryAddress(jsonData.get("comDeliveryAddress").getAsString());
                template.setDeliveryPhone(jsonData.get("comDeliveryPhone").getAsString());
                template.setDeliveryName(jsonData.get("comDeliveryName").getAsString());
                button = buttonService.updateButtonCommodity(button, template);
                if (null != button) {
                    result.addProperty("ErrorInfo", "");
                } else {
                    result.addProperty("ErrorInfo", "绑定失败");
                }
            } else {
                result.addProperty("ErrorInfo", "该用户未绑定该按钮");
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

    @PostMapping("/detail/furniture")
    public String getFurnDetail(@RequestBody String body){
        JsonObject jsonData = new JsonParser().parse(body).getAsJsonObject();
        JsonObject result = new JsonObject();
        if (jsonData.has("curId") && jsonData.has("buttonId")) {
            if (userButtonService.hasUserButton(jsonData.get("curId").getAsLong(), jsonData.get("buttonId").getAsLong())){
                Button button = buttonService.findButtonById(jsonData.get("buttonId").getAsLong());
                if (null != button) {
                    if (ButtonType.FORFURN == button.getButtonType()) {
                        Furniture furniture = furnitureService.findActiveFurnByButtonId(button.getButtonId());
                        if (null != furniture) {
                            result.addProperty("IsSuccess", true);
                            result.addProperty("ErrorInfo", "");
                            result.addProperty("buttonId", button.getButtonId().toString());
                            result.addProperty("buttonName", button.getButtonName());
                            result.addProperty("buttonType", new Long(button.getButtonType().ordinal()).toString());
                            result.addProperty("furnId", furniture.getFurnId().toString());
                            result.addProperty("furnName", furniture.getFurnName());
                            result.addProperty("furnType", new Long(furniture.getFurnType().ordinal()).toString());
                            result.addProperty("furnState", new Long(furniture.getState().ordinal()).toString());
                        } else {
                            result.addProperty("ErrorInfo","未找到匹配家具");
                        }
                    } else {
                        result.addProperty("ErrorInfo", "按钮类型不匹配");
                    }
                } else {
                    result.addProperty("ErrorInfo", "非法buttonId");
                }
            } else {
                result.addProperty("ErrorInfo", "该用户未绑定该按钮");
            }
        } else {
            result.addProperty("ErrorInfo", "参数不完整");
        }
        if (!result.get("ErrorInfo").getAsString().equals("")) {
            result.addProperty("IsSuccess", false);
            result.addProperty("buttonId", "");
            result.addProperty("buttonName", "");
            result.addProperty("buttonType", "");
            result.addProperty("furnId", "");
            result.addProperty("furnName", "");
            result.addProperty("furnType", "");
            result.addProperty("furnState", "");
        }
        return result.toString();
    }

    @PostMapping("/detail/commodity")
    public String getComDetail(@RequestBody String body){
        JsonObject jsonData = new JsonParser().parse(body).getAsJsonObject();
        JsonObject result = new JsonObject();
        if (jsonData.has("curId") && jsonData.has("buttonId")) {
            if( userButtonService.hasUserButton(jsonData.get("curId").getAsLong(), jsonData.get("buttonId").getAsLong())) {
                Button button = buttonService.findButtonById(jsonData.get("buttonId").getAsLong());
                if(null != button) {
                    if (ButtonType.FORSHOP == button.getButtonType()) {
                        CommodityOrderTemplate template = commodityOrderTemplateService.findActiveTemplateByButtonId(button.getButtonId());
                        Commodity commodity = commodityOrderTemplateService.findActiveCommodityByButtonId(button.getButtonId());
                        if (null != template && null != commodity) {
                            result.addProperty("IsSuccess", true);
                            result.addProperty("ErrorInfo", "");
                            result.addProperty("buttonId", button.getButtonId().toString());
                            result.addProperty("buttonName", button.getButtonName());
                            result.addProperty("buttonType", new Long(button.getButtonType().ordinal()).toString());
                            result.addProperty("comId", commodity.getComId().toString());
                            result.addProperty("comNo", commodity.getComNo());
                            result.addProperty("comName", commodity.getComName());
                            result.addProperty("comPrice", commodity.getPrice().toString());
                            result.addProperty("comNum", template.getNum().toString());
                            result.addProperty("comDeliveryPhone", template.getDeliveryPhone());
                            result.addProperty("comDeliveryAddress", template.getDeliveryAddress());
                            result.addProperty("comDeliveryName", template.getDeliveryName());
                        } else {
                            result.addProperty("ErrorInfo","未找到匹配商品");
                        }
                    } else {
                        result.addProperty("ErrorInfo", "按钮类型不匹配");
                    }
                } else {
                    result.addProperty("ErrorInfo", "非法buttonId");
                }
            } else {
                result.addProperty("ErrorInfo", "该用户未绑定该按钮");
            }
        } else {
            result.addProperty("ErrorInfo", "参数不完整");
        }
        if(!result.get("ErrorInfo").getAsString().equals("")){
            result.addProperty("IsSuccess", false);
            result.addProperty("buttonId", "");
            result.addProperty("buttonName", "");
            result.addProperty("buttonType", "");
            result.addProperty("comId", "");
            result.addProperty("comNo", "");
            result.addProperty("comName", "");
            result.addProperty("comPrice", "");
            result.addProperty("comNum", "");
            result.addProperty("comDeliveryPhone", "");
            result.addProperty("comDeliveryAddress", "");
            result.addProperty("comDeliveryName", "");
        }
        return result.toString();
    }

    @PostMapping("/onclick")
    public String clickButton(@RequestBody String body){
        JsonObject jsonData = new JsonParser().parse(body).getAsJsonObject();
        JsonObject result = new JsonObject();
        if (jsonData.has("buttonId")) {
            Button button = buttonService.findButtonById(jsonData.get("buttonId").getAsLong());
            if (null != button) {
                String changeInfo = "";
                switch (button.getButtonType()) {
                    case UNKNOW:
                        break;
                    case FORFURN:
                        changeInfo = furnitureService.changFurnStateByButtonId(button.getButtonId());
                        break;
                    case FORSHOP:
                        changeInfo = commodityService.addOrderFromTemplateByButtonId(button.getButtonId());
                        break;
                    case FORHELP:
                        // todo
                        break;
                    case FORMONITOR:
                        break;
                }
                result.addProperty("ErrorInfo", changeInfo);
            } else {
                result.addProperty("ErrorInfo", "不存在该按钮");
            }
        } else {
            result.addProperty("ErrorInfo", "参数不完整");
        }
        if (!result.get("ErrorInfo").getAsString().equals("")) {
            result.addProperty("IsSuccess", false);
        } else {
            result.addProperty("IsSuccess", true);
        }
        return result.toString();
    }
}
