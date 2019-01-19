package com.ai.eis.mapper;

import java.util.List;

 
import com.ai.eis.model.EisRole;

public interface EisRoleMapper {
    int deleteByPrimaryKey(Integer roleId);

    int insert(EisRole record);

    int insertSelective(EisRole record);

    EisRole selectByPrimaryKey(Integer roleId);
    
    EisRole selectByRoleName(String name);
    
    int updateByPrimaryKeySelective(EisRole record);

    int updateByPrimaryKey(EisRole record);
    
    List<EisRole> selectAllRoles();
    
    
    
}