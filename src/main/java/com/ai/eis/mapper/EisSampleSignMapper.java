package com.ai.eis.mapper;

import com.ai.eis.model.EisSampleSign;

import java.util.List;
import java.util.Map;

public interface EisSampleSignMapper {
    int insert(EisSampleSign record);

    int insertSelective(EisSampleSign record);

    int deleteById(Integer id);

    List <Map <String, Object>> listProject(Map <String, String> map);

    List <EisSampleSign> listSignRecord(Map <String, String> map);
}