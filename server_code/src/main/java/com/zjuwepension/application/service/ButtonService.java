package com.zjuwepension.application.service;

import com.zjuwepension.application.entity.Button;

import java.util.List;

public interface ButtonService {
    Button saveButton(Button button);
    Button updateButton(Button button);
    Button findButtonById(Long id);
}
