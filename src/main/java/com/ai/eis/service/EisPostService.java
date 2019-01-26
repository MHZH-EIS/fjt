package com.ai.eis.service;

import java.util.List;
import java.util.Map;

import com.ai.eis.model.EisPost;

public interface EisPostService {
	int add(EisPost eispost);
    List<EisPost> queryByCondition(Map<String,String> conditions);
    
    EisPost  selectByPrimaryKey(Integer postid);
    List<EisPost>  selectByAccount(String name);
    
    int update(EisPost post);
    int deletePost(Integer postid);
}
