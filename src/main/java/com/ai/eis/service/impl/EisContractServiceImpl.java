package com.ai.eis.service.impl;

import com.ai.eis.mapper.EisContractMapper;
import com.ai.eis.model.EisContract;
import com.ai.eis.service.EisContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service(value = "EisContractService")
public class EisContractServiceImpl implements EisContractService {

    @Autowired
    private EisContractMapper contractMapper;

    @Override
    public void insertContract(EisContract contract) {
        contractMapper.insert(contract);
    }

    @Override
    public EisContract selectByPrimaryKey(Integer projectId) {
        return contractMapper.selectByPrimaryKey(projectId);
    }

    @Override
    public int getTotalSampleNum(Integer projectId) {
        return contractMapper.getTotalSampleNum(projectId);
    }

    @Override
    public List <EisContract> queryByCondition(Map <String, String> map) {
        return contractMapper.queryByCondition(map);
    }

    @Override
    public void deleteByPrimaryKey(Integer projectId) {
        contractMapper.deleteByPrimaryKey(projectId);
    }

    @Override
    public void update(EisContract contract) {
        contractMapper.updateByPrimaryKeySelective(contract);
    }
}
