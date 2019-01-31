package com.ai.eis.mapper;

import com.ai.eis.model.EisItemDev;

import java.util.List;
import java.util.Map;

public interface EisItemDevMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(EisItemDev record);

    int insertSelective(EisItemDev record);

    EisItemDev selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EisItemDev record);

    int updateByPrimaryKey(EisItemDev record);

    List <EisItemDev> queryByCondition(Map <String, String> map);
    
    //通过试验项目id 去查询设备信息等
    List<Map<String,Object>> queryDisplayList(Map<String,Integer> map);

}