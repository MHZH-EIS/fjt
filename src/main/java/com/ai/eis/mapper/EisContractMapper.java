package com.ai.eis.mapper;

import java.util.List;
import java.util.Map;

import com.ai.eis.model.EisContract;

public interface EisContractMapper {

    int deleteByPrimaryKey(Integer projectId);

    int insert(EisContract record);

    int insertSelective(EisContract record);

    EisContract selectByPrimaryKey(Integer projectId);

    int updateByPrimaryKeySelective(EisContract record);

    int updateByPrimaryKey(EisContract record);

    int getTotalSampleNum(int projectId);

    List <EisContract> queryByCondition(Map <String, String> map);

}