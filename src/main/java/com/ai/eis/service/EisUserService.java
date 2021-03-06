package com.ai.eis.service;

import java.util.List;
import java.util.Map;

import com.ai.eis.common.Constants;
import com.ai.eis.model.EisRole;
import com.ai.eis.model.EisUser;

 

public interface EisUserService {
	int addUser(EisUser user);
    List<EisUser> findAllLogins(int pageNum, int pageSize);
    EisUser  selectByPrimaryKey(Integer userid);
    EisUser  selectByName(String name);
    List <EisUser> queryByCondition(Map <String, String> map);
    List <EisUser> queryEngineers(Integer roleId);

    
    int updateUser(EisUser loginUser) ;
    int deleteUser(Integer userid);
    
    int selectMaxUserId();
}
