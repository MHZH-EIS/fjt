package com.ai.eis.mapper;

import com.ai.eis.model.EisDevice;

import java.util.List;
import java.util.Map;

public interface EisDeviceMapper {

    int deleteByPrimaryKey(Integer devId);

    int insert(EisDevice record);

    int insertSelective(EisDevice record);

    EisDevice selectByPrimaryKey(Integer devId);

    int updateByPrimaryKeySelective(EisDevice record);

    int updateByPrimaryKey(EisDevice record);

    List <EisDevice> queryByCondition(Map <String, String> map);

    List <Map <String, Object>> queryDevBrief(Integer pId);

}