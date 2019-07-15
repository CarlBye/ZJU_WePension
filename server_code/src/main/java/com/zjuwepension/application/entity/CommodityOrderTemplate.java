package com.zjuwepension.application.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class CommodityOrderTemplate {
    @Id
    @GeneratedValue(generator = "tempId")
    private Long tempId;
    private Long buttonId;
    private Long comId;
    private Long num;
    private String deliveryAddress;
    private String deliveryPhone;
    private String deliveryName;
    private String date;
    private Boolean isActive;
}
