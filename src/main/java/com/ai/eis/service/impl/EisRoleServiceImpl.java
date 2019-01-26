package com.ai.eis.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.eis.mapper.EisRoleMapper;
 
import com.ai.eis.model.EisRole;
import com.ai.eis.service.EisRoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


@Service(value = "eisRoleService")
public class EisRoleServiceImpl implements EisRoleService {


    @Autowired
    private EisRoleMapper eisRoleMapper;
	
	@Override
	public int addRole(EisRole role) {
		 
		return eisRoleMapper.insertSelective(role);
	}

	@Override
	public List<EisRole> findAllRoles(int pageNum, int pageSize) {
        //将参数传给这个方法就可以实现物理分页了，非常简单。
        PageHelper.startPage(pageNum, pageSize);
        List<EisRole> roles = eisRoleMapper.selectAllRoles();
        //PageInfo<EisRole> pageInfo = new PageInfo<>(roles);
        //return pageInfo;
		return roles;
        //return eisRoleMapper.selectAllRoles();
	}

	@Override
	public EisRole selectByPrimaryKey(Integer roleId) {
		 
		return eisRoleMapper.selectByPrimaryKey(roleId);
	}

	@Override
	public EisRole selectByRoleName(String name) {
		 
		return eisRoleMapper.selectByRoleName(name);
	}

	@Override
	public List<EisRole> findAll() {
		return eisRoleMapper.selectAllRoles();
	}

	@Override
	public int deleteRole(Integer role_id) {
		return eisRoleMapper.deleteByPrimaryKey(role_id);
	}

	@Override
	public int updateRole(EisRole record) {
 
		return eisRoleMapper.updateByPrimaryKey(record);
	}

}
