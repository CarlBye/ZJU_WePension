package com.zjuwepension.application.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Furniture {
    @Id
    @GeneratedValue
    private Long furnId;
    private String furnName;
    @Enumerated(EnumType.ORDINAL)
    private FurnStateType state;
    @Enumerated(EnumType.ORDINAL)
    private FurnType furnType;
}
