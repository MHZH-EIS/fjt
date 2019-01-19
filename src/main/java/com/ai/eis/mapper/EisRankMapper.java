package com.ai.eis.mapper;

import com.ai.eis.model.EisRank;

public interface EisRankMapper {
    int deleteByPrimaryKey(Integer rankId);

    int insert(EisRank record);

    int insertSelective(EisRank record);

    EisRank selectByPrimaryKey(Integer rankId);

    int updateByPrimaryKeySelective(EisRank record);

    int updateByPrimaryKey(EisRank record);
}