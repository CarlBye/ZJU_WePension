package com.zjuwepension.application.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class CommodityOrder {
    @Id
    @GeneratedValue(generator = "orderId")
    private Long orderId;
    private Long userId;
    private Long tempId;
    private String date;
    @Enumerated(EnumType.ORDINAL)
    private OrderStateType orderState;
}
