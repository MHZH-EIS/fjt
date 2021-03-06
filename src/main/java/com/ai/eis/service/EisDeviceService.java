package com.ai.eis.service;

import com.ai.eis.model.EisDevice;

import java.util.List;
import java.util.Map;

public interface EisDeviceService {

    void insert(EisDevice device);

    int deleteByKey(Integer id);

    List <EisDevice> queryByCondition(Map <String, String> map);
    
    EisDevice queryById(Integer id);
    
    int updateDevice(EisDevice device);

    List <Map <String, Object>> queryDevBrief(Integer pId);

}
