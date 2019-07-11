package com.zjuwepension.application.service;

import com.zjuwepension.application.entity.Furniture;

public interface FurnitureService {
    Furniture saveFurn(Furniture furn);
    Furniture updateFurn(Furniture furn);
    Furniture findFurnByFurnId(Long id);
}
