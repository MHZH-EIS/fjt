package com.ai.eis.controller;

import com.ai.eis.common.AjaxResult;
import com.ai.eis.common.DataGrid;
import com.ai.eis.common.FileModel;
import com.ai.eis.common.Tools;
import com.ai.eis.model.EisDevice;
import com.ai.eis.model.EisSampleSend;
import com.ai.eis.model.EisSampleSign;
import com.ai.eis.service.EisSampleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void sendForm(Integer id,Model model) {
        if (id != null) {
            ObjectMapper mapper = new ObjectMapper();
            Map<String,String> conditions = new HashMap<>();
            conditions.put("id", String.valueOf(id));
            
            List<EisSampleSend> resources = sampleService.listSendRecord(conditions);
            if (resources == null|| resources.size() == 0) {
            	return;
            }
             
            try {
            	EisSampleSend resource = resources.get(0);
                model.addAttribute("resource", mapper.writeValueAsString(resource));
            } catch (JsonProcessingException e) {
                logger.error("json转换错误", e);
            }
        }
    }

    @RequestMapping("/sign/form")
    public void signForm(Integer id,Model model ) {
        if (id != null) {
            ObjectMapper mapper = new ObjectMapper();
            Map<String,String> conditions = new HashMap<>();
            conditions.put("id", String.valueOf(id));
            
            List<EisSampleSign> resources = sampleService.listSignRecord(conditions);
            if (resources == null|| resources.size() == 0) {
            	return;
            }
             
            try {
            	EisSampleSign resource = resources.get(0);
                model.addAttribute("resource", mapper.writeValueAsString(resource));
            } catch (JsonProcessingException e) {
                logger.error("json转换错误", e);
            }
        }
    }

    @RequestMapping("/project")
    public void project() {

    }

    /**
     * 发货
     *
     * @param record
     * @return
     */
    @ResponseBody
    @Transactional
    @RequestMapping({"/send/add", "/send/save"})
    public AjaxResult sendSample(@Valid EisSampleSend record, BindingResult br) {
        if (br.hasErrors()) {
            logger.error("对象校验失败：" + br.getAllErrors());
            return new AjaxResult(false).setData(br.getAllErrors());
        }
        if(record.getId() != null) {
        	sampleService.updateSend(record);
        }else {
            sampleService.send(record);
        }
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
    @RequestMapping({"/sign/add", "/sign/save"})
    public AjaxResult signSample(@Valid EisSampleSign record, BindingResult br) {
        if (br.hasErrors()) {
            logger.error("对象校验失败：" + br.getAllErrors());
            return new AjaxResult(false).setData(br.getAllErrors());
        }
        MultipartFile multipartFile = record.getEnclosureFile();

        try {
        	if (!multipartFile.isEmpty()) {
                File file = FileModel.generateSample(record.getProjectId(), multipartFile.getOriginalFilename());
                logger.info("save file path:{}", file.getAbsolutePath());
                multipartFile.transferTo(file);
                logger.info("样品附件上传成功，地址为{}", file.getAbsolutePath());
            }
        	
        	//更新操作
        	if(record.getId() != null) {
        		sampleService.updateSign(record);
        	}
        	else {
        		//新建操作
                sampleService.sign(record);
                logger.info("项目{}此次成功签收{}个样品", record.getProjectId(), record.getSingNum());
        	}

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return new AjaxResult(false).setData(e);
        }
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
    public DataGrid<EisSampleSend> listSendRecord(Integer page,Integer rows, @RequestParam(value = "projectId", defaultValue = "") String pId,
                                               @RequestParam(value = "contact", defaultValue = "") String contact) {
       	com.github.pagehelper.Page<Object> pg = PageHelper.startPage(page, rows);
    	Map <String, String> map = new HashMap <>();
        map.put("pId", pId);
        map.put("contact", Tools.liker(contact));
        List <EisSampleSend> lst =  sampleService.listSendRecord(map);
        
        DataGrid<EisSampleSend> dg = new DataGrid<EisSampleSend>(lst);
		dg.setTotal(pg.getTotal());
		return dg;
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
    public DataGrid<EisSampleSign> listSignRecord(Integer page,Integer rows,@RequestParam(value = "projectId", defaultValue = "") String pId,
                                               @RequestParam(value = "sampleName", defaultValue = "") String name) {
     	com.github.pagehelper.Page<Object> pg = PageHelper.startPage(page, rows);
    	Map <String, String> map = new HashMap <>();
        map.put("pId", pId);
        map.put("name", Tools.liker(name));
        List<EisSampleSign> lst = sampleService.listSignRecord(map);
        DataGrid<EisSampleSign> dg = new DataGrid<EisSampleSign>(lst);
    	dg.setTotal(pg.getTotal());
		return dg;

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
    public DataGrid <Map <String, Object>> listProject(Integer page,Integer rows,@RequestParam(value = "projectName", defaultValue = "") String name,
                                                   @RequestParam(value = "status", defaultValue = "") String status,
                                                   @RequestParam(value = "projectNo", defaultValue = "") String pNo) {
    	com.github.pagehelper.Page<Object> pg = PageHelper.startPage(page, rows);
    	Map <String, String> map = new HashMap <>();
        map.put("name", Tools.liker(name));
        map.put("status", status);
        map.put("pNo", Tools.liker(pNo));
        List <Map <String, Object>> lst =  sampleService.listProject(map);
        DataGrid<Map <String, Object>> dg = new DataGrid<Map <String, Object>>(lst);
    	dg.setTotal(pg.getTotal());
    	return dg;
    }


}
