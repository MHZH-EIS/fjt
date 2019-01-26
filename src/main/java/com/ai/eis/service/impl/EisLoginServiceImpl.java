package com.ai.eis.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.eis.mapper.EisLoginMapper;
 
import com.ai.eis.model.EisLogin;
import com.ai.eis.service.EisLoginService;
import com.github.pagehelper.PageHelper;

@Service(value = "eisLoginService")
public class EisLoginServiceImpl implements EisLoginService{
    @Autowired
    private EisLoginMapper loginMapper;
    
    
	@Override
	public int addLogin(EisLogin loginUser) {
		return loginMapper.insert(loginUser);
	}

	@Override
	public List<EisLogin> findAllLogins(int pageNum, int pageSize) {
        //将参数传给这个方法就可以实现物理分页了，非常简单。
        PageHelper.startPage(pageNum, pageSize);
		return loginMapper.selectAllEisLogins();
	}

	@Override
	public EisLogin selectByPrimaryKey(Integer userid) {
		return loginMapper.selectByPrimaryKey(userid);
	}

	@Override
	public EisLogin selectByAccount(String name) {
		return loginMapper.selectByAccount(name);
	}

	@Override
	public int updateLogin(EisLogin loginUser) {
	 
		return loginMapper.updateByPrimaryKey(loginUser);
	}

}
