package com.ai.eis.service;

import com.ai.eis.model.EisItemDev;

import java.util.List;
import java.util.Map;

public interface DevExService {

    int insertSelective(EisItemDev record);

    List<EisItemDev> queryByCondition(Map<String, String> map);
    
    Map<String,String> queryDisplayList(Integer id);

}
