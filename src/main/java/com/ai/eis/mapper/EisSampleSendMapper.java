package com.ai.eis.mapper;

import com.ai.eis.model.EisSampleSend;

import java.util.List;
import java.util.Map;

public interface EisSampleSendMapper {

    int insert(EisSampleSend record);

    int insertSelective(EisSampleSend record);

    int deleteById(Integer id);

    List <EisSampleSend> listSendRecord(Map <String, String> map);

	int updateSend(EisSampleSend sendSample);
}