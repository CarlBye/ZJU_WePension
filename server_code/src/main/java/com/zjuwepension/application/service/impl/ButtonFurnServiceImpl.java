package com.zjuwepension.application.service.impl;

import com.zjuwepension.application.entity.ButtonFurn;
import com.zjuwepension.application.repository.ButtonFurnRepository;
import com.zjuwepension.application.service.ButtonFurnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ButtonFurnServiceImpl implements ButtonFurnService {
    @Autowired
    private ButtonFurnRepository buttonFurnRepository;

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public ButtonFurn saveButtonFurn(ButtonFurn buttonFurn){
        return  buttonFurnRepository.save(buttonFurn);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public ButtonFurn updateButtonFurn(ButtonFurn buttonFurn){
        return buttonFurnRepository.save(buttonFurn);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED, readOnly = true)
    @Override
    public ButtonFurn findActiveButtonFurnByButtonId(Long buttonId){
        List<ButtonFurn> list = buttonFurnRepository.findButtonFurnsByButtonIdAndIsActive(buttonId, true);
        if (null != list && 1 == list.size()) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED, readOnly = true)
    @Override
    public ButtonFurn findActiveButtonFurnByFurnId(Long furnId){
        List<ButtonFurn> list = buttonFurnRepository.findButtonFurnsByFurnIdAndIsActive(furnId, true);
        if (null != list && 1 == list.size()) {
            return list.get(0);
        } else {
            return null;
        }
    }


}
