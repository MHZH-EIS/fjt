package com.ai.eis.service;

import java.util.List;
import java.util.Map;

import com.ai.eis.model.EisPost;

public interface EisPostService {
	int add(EisPost eispost);
    List<EisPost> findAll(Map<String,String> conditions);
    
    EisPost  selectByPrimaryKey(Integer postid);
    List<EisPost>  selectByAccount(String name);
    
    int updateLogin(EisPost loginUser);
}
