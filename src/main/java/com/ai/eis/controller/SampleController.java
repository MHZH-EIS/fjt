package com.ai.eis.controller;

import com.ai.eis.common.AjaxResult;
import com.ai.eis.common.Tools;
import com.ai.eis.model.EisSampleSend;
import com.ai.eis.model.EisSampleSign;
import com.ai.eis.service.EisSampleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/resource/sample")
public class SampleController {

    private Logger logger = LoggerFactory.getLogger(SampleController.class);

    @Autowired
    private EisSampleService sampleService;

    @ResponseBody
    @RequestMapping("/send")
    public AjaxResult sendSample(@RequestBody EisSampleSend record) {
        sampleService.send(record);
        logger.info("send {} smaple success,the project is{}", record.getSendNum(), record.getProjectId());
        return new AjaxResult(true);
    }

    @ResponseBody
    @RequestMapping("/sign")
    public AjaxResult signSample(@RequestBody EisSampleSign record) {
        sampleService.sign(record);
        logger.info("项目{}此次成功签收{}个样品", record.getProjectId(), record.getSingNum());
        return new AjaxResult(true);
    }


    @ResponseBody
    @RequestMapping("listsend")
    public List <EisSampleSend> listSendRecord(String pId) {
        Map <String, String> map = new HashMap <>();
        map.put("pId", pId);
        return sampleService.listSendRecord(map);
    }

    @ResponseBody
    @RequestMapping("listsign")
    public List <EisSampleSign> listSignRecord(String pId, String name) {
        Map <String, String> map = new HashMap <>();
        map.put("pId", pId);
        map.put("name", name);
        return sampleService.listSignRecord(map);

    }

    @RequestMapping("/listproject")
    @ResponseBody
    public List <Map <String, Object>> listProject(String name) {
        Map <String, String> map = new HashMap <>();
        map.put("name", Tools.liker(name));
        return sampleService.listProject(map);
    }


}
