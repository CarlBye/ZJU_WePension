package com.zjuwepension.application.service.impl;

import com.zjuwepension.application.entity.Furniture;
import com.zjuwepension.application.repository.FurnitureRepository;
import com.zjuwepension.application.service.FurnitureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FurnitureServiceImpl implements FurnitureService {
    @Autowired
    private FurnitureRepository furnitureRepository;

    @Override
    public Furniture saveFurn(Furniture furn){
        return furnitureRepository.save(furn);
    }

    @Override
    public Furniture updateFurn(Furniture furn){
        return furnitureRepository.save(furn);
    }

    @Override
    public Furniture findFurnByFurnId(Long id){
        List<Furniture> furnitureList = furnitureRepository.findFurnituresByFurnId(id);
        if (null != furnitureList && 1 == furnitureList.size()) {
            return furnitureList.get(0);
        } else {
            return null;
        }
    }
}
