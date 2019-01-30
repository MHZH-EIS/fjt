package com.ai.eis.mapper;

import com.ai.eis.model.EisItemDev;

public interface EisItemDevMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EisItemDev record);

    int insertSelective(EisItemDev record);

    EisItemDev selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EisItemDev record);

    int updateByPrimaryKey(EisItemDev record);
}