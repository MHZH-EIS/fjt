package com.ai.eis.mapper;

import com.ai.eis.model.EisStandard;

import java.util.List;
import java.util.Map;

public interface EisStandardMapper {
    int deleteByPrimaryKey(Integer stId);

    int insert(EisStandard record);

    int insertSelective(EisStandard record);

    EisStandard selectByPrimaryKey(Integer stId);

    int updateByPrimaryKeySelective(EisStandard record);

    int updateByPrimaryKey(EisStandard record);

    List <EisStandard> list(Map <String, String> map);
}