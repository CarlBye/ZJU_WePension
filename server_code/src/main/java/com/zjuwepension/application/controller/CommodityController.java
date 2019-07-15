package com.zjuwepension.application.controller;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zjuwepension.application.entity.Commodity;
import com.zjuwepension.application.entity.CommodityOrder;
import com.zjuwepension.application.entity.CommodityOrderTemplate;
import com.zjuwepension.application.entity.RankType;
import com.zjuwepension.application.service.CommodityOrderService;
import com.zjuwepension.application.service.CommodityOrderTemplateService;
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
    @Autowired
    private CommodityOrderService commodityOrderService;
    @Autowired
    private CommodityOrderTemplateService commodityOrderTemplateService;
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
            result.get("commodityList").getAsJsonObject().addProperty("num", new Long(list.size()).toString());
            result.get("commodityList").getAsJsonObject().add("list", new JsonArray());
            for (int i = 0; i < list.size(); i++) {
                JsonObject comElement = new JsonObject();
                Commodity commodity = list.get(i);
                comElement.addProperty("comId", String.format("%08d", commodity.getComId()));
                comElement.addProperty("comNo", commodity.getComNo());
                comElement.addProperty("comName", commodity.getComName());
                comElement.addProperty("comType", commodity.getComType());
                comElement.addProperty("comDescription", commodity.getDescription());
                comElement.addProperty("comImgPath", commodity.getImgPath());
                Double price = commodity.getPrice() * 1.0 / 100.0;
                comElement.addProperty("comPrice", String.format("%.2f", price));
                comElement.addProperty("comStack",commodity.getStack().toString());
                result.get("commodityList").getAsJsonObject().get("list").getAsJsonArray().add(comElement);
            }
        } else {
            result.addProperty("IsSuccess", false);
            result.addProperty("ErrorInfo", "查找商品失败");
        }
        return result.toString();
    }

    @PostMapping("/update")
    public String updateCommodity(@RequestBody String body){
        JsonObject jsonDate = new JsonParser().parse(body).getAsJsonObject();
        JsonObject result = new JsonObject();
        if (jsonDate.has("comId") && jsonDate.has("comStack") && jsonDate.has("comPrice")){
            String info = commodityService.updateCommodityByComId(jsonDate.get("comId").getAsLong(),
                                                                    jsonDate.get("comStack").getAsLong(),
                                                                    Math.round(jsonDate.get("comPrice").getAsDouble() * 100));
            result.addProperty("ErrorInfo", info);
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

    @PostMapping("/listByName")
    public String getComListByName(@RequestBody String body){
        JsonObject jsonDate = new JsonParser().parse(body).getAsJsonObject();
        JsonObject result = new JsonObject();
        if (jsonDate.has("comName")) {
            List<Commodity> list = commodityService.getComListContaningName(jsonDate.get("comName").getAsString());
            if (null != list) {
                result.addProperty("IsSuccess", true);
                result.addProperty("ErrorInfo", "");
                result.add("commodityList", new JsonObject());
                result.get("commodityList").getAsJsonObject().addProperty("num", new Long(list.size()).toString());
                result.get("commodityList").getAsJsonObject().add("list", new JsonArray());
                for (int i = 0; i < list.size(); i++) {
                    JsonObject comElement = new JsonObject();
                    Commodity commodity = list.get(i);
                    comElement.addProperty("comId", String.format("%08d", commodity.getComId()));
                    comElement.addProperty("comNo", commodity.getComNo());
                    comElement.addProperty("comName", commodity.getComName());
                    comElement.addProperty("comType", commodity.getComType());
                    comElement.addProperty("comDescription", commodity.getDescription());
                    comElement.addProperty("comImgPath", commodity.getImgPath());
                    Double price = commodity.getPrice() * 1.0 / 100.0;
                    comElement.addProperty("comPrice", String.format("%.2f", price));
                    comElement.addProperty("comStack",commodity.getStack().toString());
                    result.get("commodityList").getAsJsonObject().get("list").getAsJsonArray().add(comElement);
                }
            } else {
                result.addProperty("ErrorInfo", "查找失败");
            }
        } else {
            result.addProperty("ErrorInfo", "参数不完整");
        }
        if (!result.get("ErrorInfo").getAsString().equals("")) {
            result.addProperty("IsSuccess", false);
        }

        return result.toString();
    }

    @PostMapping("/orderList")
    public String getOrderList(@RequestBody String body){
        JsonObject jsonDate = new JsonParser().parse(body).getAsJsonObject();
        JsonObject result = new JsonObject();
        if (jsonDate.has("curId")) {
            List<CommodityOrder> orderList = commodityOrderService.getAllOrderByUserId(jsonDate.get("curId").getAsLong());
            if (null != orderList) {
                result.addProperty("IsSuccess", true);
                result.addProperty("ErrorInfo", "");
                result.add("orderList", new JsonObject());
                result.get("orderList").getAsJsonObject().addProperty("num", new Long(orderList.size()).toString());
                result.get("orderList").getAsJsonObject().add("list", new JsonArray());
                for (int i = 0; i < orderList.size(); i++) {
                    JsonObject orderElement = new JsonObject();
                    CommodityOrder order = orderList.get(i);
                    orderElement.addProperty("orderId", order.getOrderId().toString());
                    orderElement.addProperty("orderDate", order.getDate());
                    orderElement.addProperty("orderState", new Long(order.getOrderState().ordinal()).toString());
                    CommodityOrderTemplate template = commodityOrderTemplateService.findTemplateByTemplateId(order.getTempId());
                    if (null == template){
                        result.addProperty("ErrorInfo", "不存在该订单模板");
                        break;
                    }
                    orderElement.addProperty("orderNum", template.getNum().toString());
                    orderElement.addProperty("orderDeliveryPhone", template.getDeliveryPhone());
                    orderElement.addProperty("orderDeliveryAddress", template.getDeliveryAddress());
                    orderElement.addProperty("orderDeliveryName", template.getDeliveryName());
                    Commodity commodity = commodityService.findCommodityByComId(template.getComId());
                    if (null == commodity){
                        result.addProperty("ErrorInfo", "不存在该商品");
                        break;
                    }
                    orderElement.addProperty("comId", commodity.getComId().toString());
                    orderElement.addProperty("conNo", commodity.getComNo());
                    orderElement.addProperty("comName", commodity.getComName());
                    Double price = commodity.getPrice() * 1.0 / 100.0;
                    orderElement.addProperty("comPrice", String.format("%.2f", price));
                    orderElement.addProperty("comDescription", commodity.getDescription());
                    orderElement.addProperty("comImgPath", commodity.getImgPath());
                    result.get("orderList").getAsJsonObject().get("list").getAsJsonArray().add(orderElement);
                }
            } else {
                result.addProperty("ErrorInfo", "非法ID");
            }
        } else {
            result.addProperty("ErrorInfo", "参数不完整");
        }
        if (!result.get("ErrorInfo").getAsString().equals("")) {
            result.addProperty("IsSuccess", false);
            result.addProperty("orderList", "");
        }

        return result.toString();
    }

    @PostMapping("/changeOrderState")
    public String changeOrderState(@RequestBody String body){
        JsonObject jsonDate = new JsonParser().parse(body).getAsJsonObject();
        JsonObject result = new JsonObject();
        if (jsonDate.has("curId") && jsonDate.has("orderId")) {
            String info = commodityOrderService.changeOrderState(jsonDate.get("curId").getAsLong(), jsonDate.get("orderId").getAsLong());
            result.addProperty("ErrorInfo", info);
        } else {
            result.addProperty("ErrorInfo", "参数不完整");
        }
        if(!result.get("ErrorInfo").getAsString().equals("")){
            result.addProperty("IsSuccess", false);
        } else {
            result.addProperty("IsSuccess", true);
        }
        return result.toString();
    }
}
