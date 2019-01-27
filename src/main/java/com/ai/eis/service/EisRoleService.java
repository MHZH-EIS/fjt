package com.ai.eis.service;

import java.util.List;

import com.ai.eis.model.EisRole;

 

public interface EisRoleService {
	int addRole(EisRole role);
	int deleteRole(Integer role_id);
	int updateRole(EisRole record);
	
    List<EisRole> findAllRoles(int pageNum, int pageSize);
    EisRole  selectByPrimaryKey(Integer roleId);
    EisRole  selectByRoleName(String name);
    List<EisRole> findAll();
    
 
}
