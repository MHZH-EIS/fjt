package com.ai.eis.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.eis.common.Tools;
import com.ai.eis.mapper.EisRankMapper;
import com.ai.eis.model.EisRank;
import com.ai.eis.service.EisRankService;

@Service(value = "EisRankService")
public class EisRankServiceImpl implements EisRankService {

    @Autowired
    EisRankMapper eisrankMapper;
    
    
	@Override
	public int add(EisRank eisrank) {
		 
		return eisrankMapper.insert(eisrank);
	}

	@Override
	public List<EisRank>  queryByCondition(Map<String,String> conditions){
 
		return eisrankMapper.queryByCondition(conditions);
	}

	@Override
	public EisRank selectByPrimaryKey(Integer rankId) {
 
		return eisrankMapper.selectByPrimaryKey(rankId);
	}

	@Override
	public List<EisRank> selectByAccount(String name) {
        Map<String,String> conds = new HashMap<>();
        conds.put("account", Tools.liker(name));
		return eisrankMapper.queryByCondition(conds);
	}

	@Override
	public int update(EisRank record) {
		return eisrankMapper.updateByPrimaryKey(record);
	}

	@Override
	public int delete(Integer rankId) {
 
		return eisrankMapper.deleteByPrimaryKey(rankId);
	}

}
