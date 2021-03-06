package com.ai.eis.service;

import com.ai.eis.model.EisStandard;

import java.util.List;
import java.util.Map;

public interface StandardService {

    List <EisStandard> list(Map <String, String> map);

    void add(EisStandard standard);

    void delete(Integer id);

    EisStandard queryById(Integer id);

    List <Map <String, Object>> queryByProject(Integer projectId);
    
    int update(EisStandard standard);
}
