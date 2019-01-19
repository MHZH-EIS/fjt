package com.ai.eis.service;

import java.util.List;

import com.ai.eis.model.EisUser;

 

public interface EisUserService {
	int addUser(EisUser user);
    List<EisUser> findAllLogins(int pageNum, int pageSize);
    
    EisUser  selectByPrimaryKey(Integer userid);
    EisUser  selectByName(String name);
}
