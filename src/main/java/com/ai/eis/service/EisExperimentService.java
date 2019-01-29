package com.ai.eis.service;

import com.ai.eis.model.EisExperiment;

import java.util.List;
import java.util.Map;

public interface EisExperimentService {

    void insert(EisExperiment experiment);

    int deleteByCondition(Map <String, String> map);

    List <EisExperiment> queryByCondition(Map <String, String> map);
    List <Map <String, Object>> queryExperimentBrief(Integer projectId);
    int update(EisExperiment experiment);
}
