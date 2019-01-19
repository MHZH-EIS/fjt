package com.ai.eis.mapper;

import com.ai.eis.model.EisExperiment;

public interface EisExperimentMapper {

    int insert(EisExperiment record);

    int insertSelective(EisExperiment record);

}