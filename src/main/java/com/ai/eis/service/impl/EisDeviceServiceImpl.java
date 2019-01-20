package com.ai.eis.service.impl;

import com.ai.eis.mapper.EisDeviceMapper;
import com.ai.eis.model.EisDevice;
import com.ai.eis.service.EisDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service(value = "EisDeviceService")
public class EisDeviceServiceImpl implements EisDeviceService {

    @Autowired
    private EisDeviceMapper deviceMapper;

    @Override
    public void insert(EisDevice device) {
        deviceMapper.insertSelective(device);
    }

    @Override
    public List <EisDevice> queryByCondition(Map <String, String> map) {
        return deviceMapper.queryByCondition();
    }
}
