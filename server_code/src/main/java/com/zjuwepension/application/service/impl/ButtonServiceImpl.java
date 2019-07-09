package com.zjuwepension.application.service.impl;

import com.zjuwepension.application.entity.Button;
import com.zjuwepension.application.repository.ButtonRepository;
import com.zjuwepension.application.service.ButtonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ButtonServiceImpl implements ButtonService {
    @Autowired
    private ButtonRepository buttonRepository;

    @Override
    public Button saveButton(Button button){
        return buttonRepository.save(button);
    }

    @Override
    public Button updateButton(Button button){
        return buttonRepository.save(button);
    }

    @Override
    public Button findButtonById(Long id){
        List<Button> buttonList = buttonRepository.findButtonsByButtonId(id);
        if(null != buttonList && 1 == buttonList.size()){
            return buttonList.get(0);
        }else {
            return null;
        }
    }
}
