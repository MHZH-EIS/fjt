package com.ai.eis.mapper;

import com.ai.eis.model.EisStItem;

import java.util.List;
import java.util.Map;

public interface EisStItemMapper {
    int deleteByPrimaryKey(Integer itemId);

    int deleteByStandardId(Integer sId);

    int insert(EisStItem record);

    int insertSelective(EisStItem record);

    EisStItem selectByPrimaryKey(Integer itemId);

    int updateByPrimaryKeySelective(EisStItem record);

    int updateByPrimaryKey(EisStItem record);

    List <EisStItem> queryByCondition(Map <String, String> map);
}