package com.zjuwepension.application.controller;


import com.zjuwepension.application.service.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/commodity")
public class CommodityController {
    @Autowired
    private CommodityService commodityService;

    @PostMapping("/new")
    public String newCommodity(@RequestParam(value = "comNo") String comNo,
                               @RequestParam(value = "comName") String comName,
                               @RequestParam(value = "comPrice") String comPrice,
                               @RequestParam(value = "comStack") String comStack,
                               @RequestParam(value = "comType") String comType,
                               @RequestParam(value = "comDescription") String comDescription,
                               @RequestParam(value = "comImg")MultipartFile comImg){

        // todo

        return "";
    }
}
