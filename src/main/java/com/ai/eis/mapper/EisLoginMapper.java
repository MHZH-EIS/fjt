package com.ai.eis.mapper;

import java.util.List;

import com.ai.eis.model.EisLogin;
import com.ai.eis.model.EisUser;
 

public interface EisLoginMapper {
    int deleteByPrimaryKey(Integer userid);

    int insert(EisLogin record);

    int insertSelective(EisLogin record);

    EisLogin selectByPrimaryKey(Integer userid);
    
    /*根据登录账号查询用户信息*/
    EisLogin selectByAccount(String useraccount);

    /*根据账号查询用户*/
    EisUser selectUserByUserid(Integer userid);
    
    int updateByPrimaryKeySelective(EisLogin record);

    int updateByPrimaryKey(EisLogin record);
    
    
    List<EisLogin> selectAllEisLogins();
    
}