package com.ai.eis.controller;

import com.ai.eis.common.AjaxResult;
import com.ai.eis.model.EisItemDev;
import com.ai.eis.service.DevExService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @ResponseBody
    @RequestMapping("/list")
    public List <EisItemDev> list(String experimentId) {
        Map <String, String> map = new HashMap <>();
        map.put("exId", experimentId);
        return devExService.queryByCondition(map);
    }

    @ResponseBody
    @RequestMapping(value ="/display/list")
    public Map<String,String> displayList(@RequestParam(value = "id", defaultValue = "") Integer id) {
    	Map<String,String> map = new HashMap<>();
    	devExService.queryDisplayList(id);
    	return map;
    }
    
}
