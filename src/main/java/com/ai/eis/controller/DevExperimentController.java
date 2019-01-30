package com.ai.eis.controller;

import com.ai.eis.common.AjaxResult;
import com.ai.eis.model.EisItemDev;
import com.ai.eis.service.DevExService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/device/experiment")
public class DevExperimentController {


    @Autowired
    private DevExService devExService;

    @RequestMapping
    public void index() {

    }

    @RequestMapping("/form")
    public void form(Long id) {
    }

    @ResponseBody
    @RequestMapping("/save")
    public AjaxResult add(EisItemDev itemDev) {
        devExService.insertSelective(itemDev);
        return new AjaxResult(true);
    }


}
