package com.ai.eis.mapper;

import com.ai.eis.model.EisCreateReports;

import java.util.List;
import java.util.Map;

public interface EisCreateReportsMapper {
    int deleteByPrimaryKey(Integer projectNo);

    int insertSelective(EisCreateReports record);

    EisCreateReports selectByPrimaryKey(Integer projectNo);


    int updateByPrimaryKeySelective(EisCreateReports record);

    int updateByPrimaryKey(EisCreateReports record);

    List<EisCreateReports> queryByCondition(Map<String, String> map);

}