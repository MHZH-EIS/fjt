package com.ai.eis.controller;

import com.ai.eis.model.EisDevice;
import com.ai.eis.service.EisDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/device")
public class DeviceController {
    private Logger logger = LoggerFactory.getLogger(DeviceController.class);

    @Autowired
    private EisDeviceService deviceService;

    @RequestMapping(value = "/add")
    public void insertDevice(@RequestBody EisDevice device) {
        deviceService.insert(device);
    }

    @ResponseBody
    @RequestMapping(value = "/getAll", produces = {"application/json;charset=UTF-8"})
    public List <EisDevice> getAll() {
        return deviceService.selectAll();
    }


}







