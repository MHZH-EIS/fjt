package com.ai.eis.controller;

import com.ai.eis.common.AjaxResult;
import com.ai.eis.common.Tools;
import com.ai.eis.model.EisContract;
import com.ai.eis.model.EisSampleSend;
import com.ai.eis.model.EisSampleSign;
import com.ai.eis.service.EisSampleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/resource/sample")
public class SampleController {

    private Logger logger = LoggerFactory.getLogger(SampleController.class);

    @Autowired
    private EisSampleService sampleService;

    @RequestMapping("/send")
    public void sendIndex() {

    }
    
    @RequestMapping("/sign")
    public void signIndex() {

    }

    @RequestMapping("/send/form")
    public void sendForm(Long id) {
    
    }
    
    @RequestMapping("/sign/form")
    public void signForm(Long id) {
    
    }
    
    /**
     * 发货
     *
     * @param record   
     * @return
     */
    @ResponseBody
    @Transactional
    @RequestMapping({"/send/add","/send/save"})
    public AjaxResult sendSample(@Valid EisSampleSend record,BindingResult br) {
        if (br.hasErrors()) {
            logger.error("对象校验失败：" + br.getAllErrors());
            return new AjaxResult(false).setData(br.getAllErrors());
        }
        
        sampleService.send(record);
        logger.info("send {} smaple success,the project is{}", record.getSendNum(), record.getProjectId());
        return new AjaxResult(true);
    }

    /**
     * 签收
     *
     * @param record
     * @return
     */
    @ResponseBody
    @Transactional
    @RequestMapping({"/sign/add","/sign/save"})
    public AjaxResult signSample(@Valid EisSampleSign record,BindingResult br) {
        if (br.hasErrors()) {
            logger.error("对象校验失败：" + br.getAllErrors());
            return new AjaxResult(false).setData(br.getAllErrors());
        }
        
        sampleService.sign(record);
        logger.info("项目{}此次成功签收{}个样品", record.getProjectId(), record.getSingNum());
        return new AjaxResult(true);
    }


    /**
     * 查询发货记录
     *
     * @param pId 项目ID
     * @return
     */
    @ResponseBody
    @RequestMapping("/send/list")
    public List <EisSampleSend> listSendRecord(@RequestParam(value = "projectId", defaultValue = "") String pId,
    		@RequestParam(value = "contact", defaultValue = "") String contact) {
        Map <String, String> map = new HashMap <>();
        map.put("pId", pId);
        map.put("contact", Tools.liker(contact));
        return sampleService.listSendRecord(map);
    }

    /**
     * 查询签收记录
     *
     * @param pId  项目ID
     * @param name 样品名称
     * @return
     */
    @ResponseBody
    @RequestMapping("/sign/list")
    public List <EisSampleSign> listSignRecord(@RequestParam(value = "projectId", defaultValue = "") String pId,
                                               @RequestParam(value = "sampleName", defaultValue = "") String name) {
        Map <String, String> map = new HashMap <>();
        map.put("pId", pId);
        map.put("name", Tools.liker(name));
        return sampleService.listSignRecord(map);

    }

    @ResponseBody
    @RequestMapping("/send/remove")
    public AjaxResult deleteSend(@RequestParam(value = "id", defaultValue = "") Integer id) {
        try {
            sampleService.deleteSendById(id);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new AjaxResult(false).setData(e);
        }
        return new AjaxResult(true);
    }

    @ResponseBody
    @RequestMapping("/sign/remove")
    public AjaxResult deleteSign(@RequestParam(value = "id", defaultValue = "") Integer id) {
        try {
            sampleService.deleteSignById(id);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new AjaxResult(false).setData(e);
        }
        return new AjaxResult(true);
    }

    /**
     * 综合关联查询项目样品到位情况
     *
     * @param name 项目名称
     * @return
     */
    @RequestMapping("/project/list")
    @ResponseBody
    public List <Map <String, Object>> listProject(@RequestParam(value = "projectName", defaultValue = "") String name) {
        Map <String, String> map = new HashMap <>();
        map.put("name", Tools.liker(name));
        return sampleService.listProject(map);
    }


}
