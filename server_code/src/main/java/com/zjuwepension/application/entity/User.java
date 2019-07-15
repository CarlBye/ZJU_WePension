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
public class User {
    @Id
    @GeneratedValue(generator = "userId")
    private Long userId;
    private String userName;
    private String userPwd;
    private String userPhoneNum;
    private String userEmail;
    private String alertPhoneNum;
    private String date;
    private String description;
    private Short faceId;
    //private String imgPath;
    // todo sex address date...
}
