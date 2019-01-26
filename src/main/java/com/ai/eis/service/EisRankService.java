package com.ai.eis.service;

import java.util.List;


import com.ai.eis.model.EisRank;

public interface EisRankService {
	int add(EisRank eisrank);
    List<EisRank> findAll(int pageNum, int pageSize);
    
    EisRank  selectByPrimaryKey(Integer userid);
    EisRank  selectByAccount(String name);
    
    int updateLogin(EisRank loginUser);
}
