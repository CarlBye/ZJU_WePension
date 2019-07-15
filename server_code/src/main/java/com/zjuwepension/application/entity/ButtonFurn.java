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
public class ButtonFurn {
    @Id
    @GeneratedValue(generator = "bindId")
    private Long bindId;
    private Long furnId;
    private Long buttonId;
    private String Date;
    private Boolean isActive;
}
