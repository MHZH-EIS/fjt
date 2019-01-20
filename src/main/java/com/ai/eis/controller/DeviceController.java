package com.ai.eis.controller;

import com.ai.eis.common.AjaxResult;
import com.ai.eis.common.Tools;
import com.ai.eis.model.EisDevice;
import com.ai.eis.service.EisDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/resource/device")
public class DeviceController {
    private Logger logger = LoggerFactory.getLogger(DeviceController.class);

    @Autowired
    private EisDeviceService deviceService;

    @RequestMapping(value = "/save")
    public AjaxResult insertDevice(EisDevice device) {
        deviceService.insert(device);
        logger.info("添加了一个新设备{}", device.getName());
        return new AjaxResult(true);
    }

    @ResponseBody
    @RequestMapping(value = "/list")
    public List <EisDevice> list(String name) {
        Map <String, String> map = new HashMap <>();
        map.put("name", Tools.liker(name));
        return deviceService.queryByCondition(map);
    }


}







