package com.ai.eis.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

 
import com.ai.eis.mapper.EisRoleMenuResourceMapper;
import com.ai.eis.model.EisRoleMenuResource;
import com.ai.eis.service.EisRoleMenuResourceService;




@Service(value = "eisRoleMenuResourceService")
public class EisRoleMenuResourceServiceImpl implements EisRoleMenuResourceService {

    @Autowired
    private EisRoleMenuResourceMapper eisRoleMenuResourceMapper;
    
    
	@Override
	public int addMenuRoleResource(EisRoleMenuResource role) {
 
		return eisRoleMenuResourceMapper.insert(role);
	}

	@Override
	public List<EisRoleMenuResource> selectByRoleId(Integer roleId) {
 
		return eisRoleMenuResourceMapper.selectByRoleId(roleId);
	}

	@Override
	public void addRoleMenu(Integer roleid, List<Long> resourceids) {
		Map<String, Object> map = new HashMap<>();
		map.put("roleid", roleid);
		map.put("resourceids", resourceids);
		
		
		eisRoleMenuResourceMapper.insertByIdAndResourceids(map);
	}

	@Override
	public int deleteRoleMenu(Integer roleid) {
 
		return eisRoleMenuResourceMapper.deleteByPrimaryKey(roleid);
		
	}

	

}
