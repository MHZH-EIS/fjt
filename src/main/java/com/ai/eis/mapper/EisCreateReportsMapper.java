package com.ai.eis.mapper;

import com.ai.eis.model.EisCreateReport;

import java.util.List;
import java.util.Map;

public interface EisCreateReportsMapper {
    int deleteByPrimaryKey(Integer projectNo);

    int insertSelective(EisCreateReport record);

    EisCreateReport selectByPrimaryKey(Integer projectNo);


    int updateByPrimaryKeySelective(EisCreateReport record);

    int updateByPrimaryKey(EisCreateReport record);

    List<EisCreateReport> queryByCondition(Map<String, String> map);

}