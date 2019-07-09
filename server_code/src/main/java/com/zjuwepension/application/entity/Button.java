package com.zjuwepension.application.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Button {
    @Id
    private Long buttonId;
    private String buttonName;
    @Enumerated(EnumType.ORDINAL)
    private ButtonType buttonType;
}
