package com.zjuwepension.application.service;

import com.zjuwepension.application.entity.ButtonFurn;

public interface ButtonFurnService {
    ButtonFurn saveButtonFurn(ButtonFurn buttonFurn);
    ButtonFurn updateButtonFurn(ButtonFurn buttonFurn);
    ButtonFurn findActiveButtonFurnByButtonId(Long buttonId);
    ButtonFurn findActiveButtonFurnByFurnId(Long furnId);
}
