package com.zjuwepension.application.service.impl;

import com.zjuwepension.application.entity.Button;
import com.zjuwepension.application.entity.ButtonFurn;
import com.zjuwepension.application.entity.ButtonType;
import com.zjuwepension.application.entity.CommodityOrderTemplate;
import com.zjuwepension.application.repository.ButtonRepository;
import com.zjuwepension.application.service.ButtonFurnService;
import com.zjuwepension.application.service.ButtonService;
import com.zjuwepension.application.service.CommodityOrderTemplateService;
import com.zjuwepension.tool.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ButtonServiceImpl implements ButtonService {
    @Autowired
    private ButtonRepository buttonRepository;
    @Autowired
    private ButtonFurnService buttonFurnService;
    @Autowired
    private CommodityOrderTemplateService commodityOrderTemplateService;

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public Button saveButton(Button button){
        return buttonRepository.save(button);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public Button updateButton(Button button){
        return buttonRepository.save(button);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED, readOnly = true)
    @Override
    public Button findButtonById(Long id){
        List<Button> buttonList = buttonRepository.findButtonsByButtonId(id);
        if(null != buttonList && 1 == buttonList.size()){
            return buttonList.get(0);
        }else {
            return null;
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public Button updateButtonFurn(Button button, Long furnId){
        if (!clearButtonHistory(button))
            return null;
        button.setButtonType(ButtonType.FORFURN);
        buttonRepository.save(button);
        if (null != buttonFurnService.findActiveButtonFurnByFurnId(furnId))
            return null;
        ButtonFurn buttonFurn = new ButtonFurn();
        buttonFurn.setButtonId(button.getButtonId());
        buttonFurn.setFurnId(furnId);
        buttonFurn.setDate(Tool.getDate());
        buttonFurn.setIsActive(true);
        buttonFurnService.saveButtonFurn(buttonFurn);
        return button;
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public Button updateButtonAlert(Button button){
        if (!clearButtonHistory(button))
            return null;
        button.setButtonType(ButtonType.FORHELP);
        buttonRepository.save(button);
        return button;
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public Button updateButtonCommodity(Button button, CommodityOrderTemplate template){
        if (!clearButtonHistory(button))
            return null;
        button.setButtonType(ButtonType.FORSHOP);
        buttonRepository.save(button);
        template.setButtonId(button.getButtonId());
        template.setDate(Tool.getDate());
        template.setIsActive(true);
        commodityOrderTemplateService.saveTemplate(template);
        return button;
    }

    private Boolean clearButtonHistory(Button button){
        if (null == button)
            return false;
        switch (button.getButtonType()){
            case FORSHOP:
                CommodityOrderTemplate template = commodityOrderTemplateService.findActiveTemplateByButtonId(button.getButtonId());
                if (null == template)
                    return null;
                template.setIsActive(false);
                commodityOrderTemplateService.updateTemplate(template);
                break;
            case FORFURN:
                ButtonFurn tempBind = buttonFurnService.findActiveButtonFurnByButtonId(button.getButtonId());
                if (null == tempBind)
                    return null;
                tempBind.setIsActive(false);
                buttonFurnService.updateButtonFurn(tempBind);
                break;
            case FORHELP:
                // no operation
                break;
            case FORMONITOR:
                // todo
                break;
        }
        return true;
    }
}
