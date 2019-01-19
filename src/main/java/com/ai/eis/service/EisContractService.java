package com.ai.eis.service;

import com.ai.eis.model.EisContract;

import java.util.List;
import java.util.Map;

public interface EisContractService {

    void insertContract(EisContract contract);

    EisContract selectByPrimaryKey(Integer projectId);

    int getTotalSampleNum(Integer projectId);

    List <EisContract> queryByCondition(Map <String, String> map);

    void deleteByPrimaryKey(Integer projectId);

    void update(EisContract contract);

}
