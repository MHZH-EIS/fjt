package com.ai.eis.controller;

import com.ai.eis.common.AjaxResult;
import com.ai.eis.common.Constants;
import com.ai.eis.common.Tools;
import com.ai.eis.model.EisDevice;
import com.ai.eis.service.EisDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @RequestMapping
    public void index() {

    }

    @RequestMapping("/form")
    public void form(Long id) {
    }

    @RequestMapping(value = "/save")
    @ResponseBody
    public AjaxResult insertDevice(EisDevice device) {
        device.setResourceId(Constants.DEVICE_RESOURCE_ID);
        deviceService.insert(device);
        logger.info("添加了一个新设备{}", device.getName());
        return new AjaxResult(true);
    }

    @ResponseBody
    @RequestMapping(value = "/list")
    public List <EisDevice> list(@RequestParam(value = "deviceType", defaultValue = "") String deviceType,
                                 @RequestParam(value = "name", defaultValue = "") String name) {
        Map <String, String> map = new HashMap <>();
        map.put("name", Tools.liker(name));
        map.put("deviceType", Tools.liker(deviceType));
        return deviceService.queryByCondition(map);
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
