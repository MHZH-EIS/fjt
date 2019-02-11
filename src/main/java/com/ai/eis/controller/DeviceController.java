package com.ai.eis.controller;

import com.ai.eis.common.AjaxResult;
import com.ai.eis.common.Constants;
import com.ai.eis.common.DataGrid;
import com.ai.eis.common.Tools;
import com.ai.eis.model.EisDevice;
import com.ai.eis.model.EisUser;
import com.ai.eis.service.EisDeviceService;
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

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/resource/device")
public class DeviceController {
    private Logger logger = LoggerFactory.getLogger(DeviceController.class);

    @Autowired
    private EisDeviceService deviceService;

    @RequestMapping
    public void index() {

    }

    @RequestMapping("/form")
    public void form(Integer id, Model model) {
        if (id != null) {
            ObjectMapper mapper = new ObjectMapper();
            EisDevice resource = deviceService.queryById(id);
            resource.setId(resource.getDevId());
            try {
                model.addAttribute("resource", mapper.writeValueAsString(resource));
            } catch (JsonProcessingException e) {
                logger.error("json转换错误", e);
            }
        }
    }

    @Transactional
    @RequestMapping(value = "/save")
    @ResponseBody
    public AjaxResult insertDevice(@Valid EisDevice device, BindingResult br) {
        if (br.hasErrors()) {
            logger.error("对象校验失败：" + br.getAllErrors());
            return new AjaxResult(false).setData(br.getAllErrors());
        } else {
            if (device.getId() != null) {
                device.setDevId(device.getId());
                deviceService.updateDevice(device);
                logger.info("更新了一个新设备{}", device.getName());
            } else {
                device.setResourceId(Constants.DEVICE_RESOURCE_ID);
                deviceService.insert(device);
                logger.info("添加了一个新设备{}", device.getName());
            }
            return new AjaxResult(true);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/list")
    public DataGrid<EisDevice> list(Integer page,Integer rows,@RequestParam(value = "deviceType", defaultValue = "") String deviceType,
                                 @RequestParam(value = "name", defaultValue = "") String name,
                                 @RequestParam(value = "devNo", defaultValue = "") String devNo) {
    	com.github.pagehelper.Page<Object> pg = PageHelper.startPage(page, rows);
 
        List<EisDevice> lst = listAll(deviceType,name,devNo);
        DataGrid<EisDevice> dg = new DataGrid<EisDevice>(lst);
		dg.setTotal(pg.getTotal());
		
        return dg;
    }
    
    
    @ResponseBody
    @RequestMapping(value = "/listAll")
    public List<EisDevice> listAll( @RequestParam(value = "deviceType", defaultValue = "") String deviceType,
                                 @RequestParam(value = "name", defaultValue = "") String name,
                                 @RequestParam(value = "devNo", defaultValue = "") String devNo) {
 
        Map <String, String> map = new HashMap <>();
        map.put("name", Tools.liker(name));
        map.put("deviceType", Tools.liker(deviceType));
        map.put("devNo", Tools.liker(devNo));
        List<EisDevice> lst = deviceService.queryByCondition(map);
 
		
        return lst;
    }
    
    
   
    @ResponseBody
    @RequestMapping(value = "/remove")
    public AjaxResult remove(@RequestParam(value = "id") Integer id) {
        try {
            deviceService.deleteByKey(id);
            return new AjaxResult(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new AjaxResult(false).setData(e);
        }
    }

}
