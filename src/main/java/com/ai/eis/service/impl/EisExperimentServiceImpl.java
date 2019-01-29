package com.ai.eis.service.impl;

import com.ai.eis.mapper.EisExperimentMapper;
import com.ai.eis.model.EisExperiment;
import com.ai.eis.service.EisExperimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service(value = "EisExperimentService")
public class EisExperimentServiceImpl implements EisExperimentService {

    @Autowired
    private EisExperimentMapper experimentMapper;


    @Override
    public void insert(EisExperiment experiment) {
        experimentMapper.insertSelective(experiment);
    }

    @Override
    public int deleteByCondition(Map <String, String> map) {
        return experimentMapper.deleteByCondition(map);
    }

    @Override
    public List <EisExperiment> queryByCondition(Map <String, String> map) {
        return experimentMapper.queryByCondition(map);
    }

	@Override
	public int update(EisExperiment experiment) {
 
		return experimentMapper.updateSelective(experiment);
	}
}
