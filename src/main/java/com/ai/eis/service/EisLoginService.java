package com.ai.eis.service;

import java.util.List;

import com.ai.eis.model.*;

public interface EisLoginService {

	int addLogin(EisLogin loginUser);
    List<EisLogin> findAllLogins(int pageNum, int pageSize);
    
    EisLogin  selectByPrimaryKey(Integer userid);
    EisLogin  selectByAccount(String name);
    
    int updateLogin(EisLogin loginUser);
}
