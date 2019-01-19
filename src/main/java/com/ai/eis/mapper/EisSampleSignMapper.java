package com.ai.eis.mapper;

import com.ai.eis.model.EisSampleSign;

public interface EisSampleSignMapper {
    int insert(EisSampleSign record);

    int insertSelective(EisSampleSign record);

    int getSampleNum(Integer projectId);
}