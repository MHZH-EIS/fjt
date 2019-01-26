package com.ai.eis.mapper;

import java.util.List;
import java.util.Map;

import com.ai.eis.model.EisPost;

public interface EisPostMapper {
    int deleteByPrimaryKey(Integer postId);

    int insert(EisPost record);

    int insertSelective(EisPost record);

    EisPost selectByPrimaryKey(Integer postId);

    int updateByPrimaryKeySelective(EisPost record);

    int updateByPrimaryKey(EisPost record);
    
    List<EisPost> queryByCondition(Map<String,String> conditions);
}