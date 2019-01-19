package com.ai.eis.mapper;

import com.ai.eis.model.EisSampleSend;

public interface EisSampleSendMapper {
    int insert(EisSampleSend record);

    int insertSelective(EisSampleSend record);
}