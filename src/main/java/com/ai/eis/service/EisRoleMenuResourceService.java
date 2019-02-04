package com.ai.eis.service;

import java.util.List;


import com.ai.eis.model.EisRoleMenuResource;

 

public interface EisRoleMenuResourceService {
	int addMenuRoleResource(EisRoleMenuResource role);
	 
    List<EisRoleMenuResource>  selectByRoleId(Integer roleId);

	void addRoleMenu(Integer roleid, List<Long> resourceids);
	
	int deleteRoleMenu(Integer roleid);
}
