package com.zjuwepension.application.service.impl;

import com.zjuwepension.application.entity.ButtonFurn;
import com.zjuwepension.application.entity.FurnStateType;
import com.zjuwepension.application.entity.Furniture;
import com.zjuwepension.application.repository.FurnitureRepository;
import com.zjuwepension.application.service.ButtonFurnService;
import com.zjuwepension.application.service.FurnitureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FurnitureServiceImpl implements FurnitureService {
    @Autowired
    private FurnitureRepository furnitureRepository;
    @Autowired
    private ButtonFurnService buttonFurnService;

    @Override
    public Furniture saveFurn(Furniture furn){
        return furnitureRepository.save(furn);
    }

    @Override
    public Furniture updateFurn(Furniture furn){
        return furnitureRepository.save(furn);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public Furniture findFurnByFurnId(Long id){
        List<Furniture> furnitureList = furnitureRepository.findFurnituresByFurnId(id);
        if (null != furnitureList && 1 == furnitureList.size()) {
            return furnitureList.get(0);
        } else {
            return null;
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public Furniture findActiveFurnByButtonId(Long buttonId){
        ButtonFurn buttonFurn = buttonFurnService.findActiveButtonFurnByButtonId(buttonId);
        if (null == buttonFurn)
            return null;
        return findFurnByFurnId(buttonFurn.getFurnId());
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public String changFurnStateByButtonId(Long buttonId){
        Furniture furniture = findActiveFurnByButtonId(buttonId);
        if (null == furniture)
            return "不存在该家具";
        switch (furniture.getState()){
            case OFF:
                furniture.setState(FurnStateType.ON);
                break;
            case ON:
                furniture.setState(FurnStateType.OFF);
                break;
        }
        furniture = updateFurn(furniture);
        if (null == furniture)
            return "更新失败";
        return "";
    }
}
