package com.ai.eis.controller;

import com.ai.eis.common.AjaxResult;
import com.ai.eis.model.EisItemDev;
import com.ai.eis.service.DevExService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

@Controller
@RequestMapping("/device/experiment")
public class DevExperimentController {
    private Logger logger = LoggerFactory.getLogger(DevExperimentController.class);

    @Autowired
    private DevExService devExService;

    @RequestMapping
    public void index() {

    }

    @RequestMapping("/form")
    public void form(Long id) {
    }

    @ResponseBody
    @RequestMapping(value = "/remove")
    public AjaxResult remove(@RequestParam(value = "id") Integer id) {
        try {
        	 devExService.delete(id);
             return new AjaxResult(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new AjaxResult(false).setData(e);
        }
    }
    

    @RequestMapping("/save")
    @ResponseBody
    @Transactional
    public AjaxResult add(@Valid EisItemDev itemDev , BindingResult br) {
        if (br.hasErrors()) {
            logger.error("对象校验失败：" + br.getAllErrors());
            return new AjaxResult(false).setData(br.getAllErrors());
        }
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
    public List<Map<String,Object>> displayList( Integer id) {
    	Map<String,Integer> map = new HashMap<>();
    	map.put("id", id);
    	return devExService.queryDisplayList(map);
    }
    
}
