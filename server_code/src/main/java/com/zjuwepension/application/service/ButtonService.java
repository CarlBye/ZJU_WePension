package com.zjuwepension.application.service;

import com.zjuwepension.application.entity.Button;
import com.zjuwepension.application.entity.CommodityOrderTemplate;

import java.util.List;

public interface ButtonService {
    Button saveButton(Button button);
    Button updateButton(Button button);
    Button findButtonById(Long id);
    Button updateButtonFurn(Button button, Long furnId);
    Button updateButtonAlert(Button button);
    Button updateButtonCommodity(Button button, CommodityOrderTemplate template);
}
