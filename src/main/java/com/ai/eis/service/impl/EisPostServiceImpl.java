package com.ai.eis.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.eis.common.Tools;
import com.ai.eis.mapper.EisPostMapper;
import com.ai.eis.model.EisPost;
import com.ai.eis.service.EisPostService;

@Service(value = "EisPostService")
public class EisPostServiceImpl implements EisPostService {

    @Autowired
    EisPostMapper eispostMapper;
	
	
	@Override
	public int add(EisPost eispost) {
 
		return eispostMapper.insert(eispost);
	}

	@Override
	public List<EisPost> queryByCondition(Map<String,String> conditions ) {
		return eispostMapper.queryByCondition(conditions);
	}

	@Override
	public EisPost selectByPrimaryKey(Integer postId) {
 
		return eispostMapper.selectByPrimaryKey(postId);
	}

	@Override
	public List<EisPost> selectByAccount(String name) {
  
        Map<String,String> conditions = new HashMap<>();
        conditions.put("name", Tools.liker(name));
		return eispostMapper.queryByCondition(conditions);
	}

	@Override
	public int update(EisPost record) {
 
		return eispostMapper.updateByPrimaryKey(record);
	}

	@Override
	public int deletePost(Integer postId) {
 
		return eispostMapper.deleteByPrimaryKey(postId);
	}

}
