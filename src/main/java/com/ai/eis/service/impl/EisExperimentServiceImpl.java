package com.ai.eis.service.impl;

import com.ai.eis.mapper.EisExperimentMapper;
import com.ai.eis.model.EisExperiment;
import com.ai.eis.service.EisExperimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "EisExperimentService")
public class EisExperimentServiceImpl implements EisExperimentService {

    @Autowired
    private EisExperimentMapper experimentMapper;


    @Override
    public void insert(EisExperiment experiment) {
        experimentMapper.insert(experiment);
    }
}
