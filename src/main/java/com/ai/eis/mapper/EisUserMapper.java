package com.ai.eis.mapper;

import java.util.List;

 
import com.ai.eis.model.EisUser;

public interface EisUserMapper {
    int deleteByPrimaryKey(Integer userid);

    int insert(EisUser record);

    int insertSelective(EisUser record);

    EisUser selectByPrimaryKey(Integer userid);

    int updateByPrimaryKeySelective(EisUser record);

    int updateByPrimaryKey(EisUser record);
    
    List<EisUser> selectAllEisUsers();
    
    EisUser selectByName(String name);
}