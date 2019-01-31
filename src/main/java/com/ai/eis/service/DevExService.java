package com.ai.eis.service;

import com.ai.eis.model.EisItemDev;

import java.util.List;
import java.util.Map;

public interface DevExService {

    int insertSelective(EisItemDev record);
    int delete(Integer itemId);

    List<EisItemDev> queryByCondition(Map<String, String> map);
    
    List<Map<String,Object>> queryDisplayList( Map<String,Integer> map);

}
