package com.ai.eis.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.ai.eis.common.Tools;
import com.ai.eis.mapper.EisPostMapper;
import com.ai.eis.model.EisPost;
import com.ai.eis.service.EisPostService;

public class EisPostServiceImpl implements EisPostService {

    @Autowired
    EisPostMapper eispostMapper;
	
	
	@Override
	public int add(EisPost eispost) {
 
		return eispostMapper.insert(eispost);
	}

	@Override
	public List<EisPost> findAll(Map<String,String> conditions ) {
		return eispostMapper.selectConditions(conditions);
	}

	@Override
	public EisPost selectByPrimaryKey(Integer postId) {
 
		return eispostMapper.selectByPrimaryKey(postId);
	}

	@Override
	public List<EisPost> selectByAccount(String name) {
  
        Map<String,String> conditions = new HashMap<>();
        conditions.put("name", Tools.liker(name));
		return eispostMapper.selectConditions(conditions);
	}

	@Override
	public int updateLogin(EisPost record) {
 
		return eispostMapper.updateByPrimaryKey(record);
	}

}
