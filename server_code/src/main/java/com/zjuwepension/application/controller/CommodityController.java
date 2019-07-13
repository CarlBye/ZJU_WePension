package com.zjuwepension.application.controller;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zjuwepension.application.entity.Commodity;
import com.zjuwepension.application.entity.RankType;
import com.zjuwepension.application.service.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/commodity")
public class CommodityController {
    @Autowired
    private CommodityService commodityService;
    @Value("${server.port}")
    private String port;

    @PostMapping("/new")
    public String newCommodity(@RequestParam(value = "comNo") String comNo,
                               @RequestParam(value = "comName") String comName,
                               @RequestParam(value = "comPrice") String comPrice,
                               @RequestParam(value = "comStack") String comStack,
                               @RequestParam(value = "comType") String comType,
                               @RequestParam(value = "comDescription") String comDescription,
                               @RequestParam(value = "comImg")MultipartFile comImg){

        JsonObject result = new JsonObject();
        if (null != comNo && !comNo.equals("") &&
            null != comName && !comName.equals("") &&
            null != comPrice && !comPrice.equals("") &&
            null != comStack && !comStack.equals("") &&
            null != comType && !comType.equals("") &&
            null != comDescription && !comDescription.equals("") &&
            !comImg.isEmpty()) {
            Commodity commodity = new Commodity();
            commodity.setComNo(comNo);
            commodity.setComName(comName);
            commodity.setComRank(RankType.UNKNOW);
            commodity.setComType(comType);
            commodity.setDescription(comDescription);
            commodity.setStack(Long.parseLong(comStack));
            Double price = Double.parseDouble(comPrice) * 100.0;
            commodity.setPrice(Math.round(price));
            try{
                byte[] bytes = comImg.getBytes();
                String fileName = comImg.getOriginalFilename();
                String suffixName = fileName.substring(fileName.lastIndexOf("."));
                String pathName = System.getProperty("user.dir");
                fileName = UUID.randomUUID().toString().replace("-", "") + suffixName;
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(pathName + "/static/commodity/img/" + fileName)));
                commodity.setImgPath("http://47.100.98.181:" + port + "/commodity/img/" + fileName);
                bos.write(bytes);
                bos.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            commodity = commodityService.insertCommodity(commodity);
            if (null != commodity) {
                result.addProperty("IsSuccess", true);
                result.addProperty("ErrorInfo", "");
            } else {
                result.addProperty("IsSuccess", false);
                result.addProperty("ErrorInfo", "商品增加失败");
            }
        } else {
                result.addProperty("IsSuccess", false);
                result.addProperty("ErrorInfo", "参数不完整");
        }
        return result.toString();
    }

    @PostMapping("/list")
    public String getComList(){
        JsonObject result = new JsonObject();
        List<Commodity> list = commodityService.getAllComList();
        if (null != list) {
            result.addProperty("IsSuccess", true);
            result.addProperty("ErrorInfo", "");
            result.add("commodityList", new JsonObject());
            result.get("commodityList").getAsJsonObject().addProperty("num", list.size());
            result.get("commodityList").getAsJsonObject().add("list", new JsonArray());
            for (int i = 0; i < list.size(); i++) {
                JsonObject comElement = new JsonObject();
                Commodity commodity = list.get(i);
                comElement.addProperty("comId", commodity.getComId().toString());
                comElement.addProperty("comName", commodity.getComName());
                comElement.addProperty("comType", commodity.getComType());
                comElement.addProperty("comDescription", commodity.getDescription());
                comElement.addProperty("comImgPath", commodity.getImgPath());
                comElement.addProperty("comPrice", commodity.getPrice().toString());
                comElement.addProperty("comStack",commodity.getStack().toString());
                result.get("commodityList").getAsJsonObject().get("list").getAsJsonArray().add(comElement);
            }
        } else {
            result.addProperty("IsSuccess", false);
            result.addProperty("ErrorInfo", "查找商品失败");
        }
        return result.toString();
    }
}
