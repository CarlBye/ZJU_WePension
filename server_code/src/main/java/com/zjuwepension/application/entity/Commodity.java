package com.zjuwepension.application.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Commodity {
    @Id
    @GeneratedValue(generator = "comId")
    private Long comId;
    private Long price;
    private Long stack;
    @Enumerated(EnumType.ORDINAL)
    private RankType comRank;
    private String comType;
    private String comNo;
    private String description;
    private String comName;
    private String imgPath;
}
