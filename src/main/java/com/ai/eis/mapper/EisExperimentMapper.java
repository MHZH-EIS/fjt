package com.ai.eis.mapper;

import com.ai.eis.model.EisExperiment;

import java.util.List;
import java.util.Map;

public interface EisExperimentMapper {

    int insert(EisExperiment record);

    int insertSelective(EisExperiment record);

    int deleteByCondition(Map <String, String> map);

    List <EisExperiment> queryByCondition(Map <String, String> map);
    
    List <Map <String, Object>> queryExperimentBrief(Integer projectId);
    
    int updateSelective(EisExperiment record);

	EisExperiment selectByPrimaryKey(Integer id);
}