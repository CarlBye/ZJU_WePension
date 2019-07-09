package com.zjuwepension.application.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zjuwepension.application.service.ButtonService;
import com.zjuwepension.application.service.UserButtonService;
import com.zjuwepension.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        // todo
        return "";
    }
}
