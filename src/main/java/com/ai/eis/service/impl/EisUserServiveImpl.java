package com.ai.eis.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.eis.mapper.EisUserMapper;
import com.ai.eis.model.EisUser;
import com.ai.eis.service.EisUserService;
import com.github.pagehelper.PageHelper;



@Service(value = "eisUserService")
public class EisUserServiveImpl implements EisUserService {

    @Autowired
    private EisUserMapper eisUserMapper;
    
    
    
	@Override
	public int addUser(EisUser user) {
		return eisUserMapper.insert(user);
	}

	@Override
	public List<EisUser> findAllLogins(int pageNum, int pageSize) {
 
        //将参数传给这个方法就可以实现物理分页了，非常简单。
        PageHelper.startPage(pageNum, pageSize);
		return eisUserMapper.selectAllEisUsers();
	}

	@Override
	public EisUser selectByPrimaryKey(Integer userid) {
 
		return eisUserMapper.selectByPrimaryKey(userid);
	}

	@Override
	public EisUser selectByName(String name) {
		return eisUserMapper.selectByName(name);
	}

	@Override
	public List<EisUser> queryByCondition(Map<String, String> map) {
 
		return eisUserMapper.queryByCondition(map);
	}

	@Override
	public int updateUser(EisUser loginUser) {
 
		return eisUserMapper.updateByPrimaryKey(loginUser);
	}

	@Override
	public int deleteUser(Integer userid) {
		 
		return eisUserMapper.deleteByPrimaryKey(userid);
	}

	@Override
	public int selectMaxUserId() {
		return eisUserMapper.selectMaxUserId();
	}

}
