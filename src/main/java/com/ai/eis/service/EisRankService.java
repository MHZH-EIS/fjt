package com.ai.eis.service;

import java.util.List;
import java.util.Map;

import com.ai.eis.model.EisRank;

public interface EisRankService {
	int add(EisRank eisrank);
    List<EisRank> queryByCondition(Map<String,String> conditions);
    
    EisRank  selectByPrimaryKey(Integer userid);
    List<EisRank>  selectByAccount(String name);
    
    int update(EisRank loginUser);
    
    int delete(Integer rankId);
    
}
