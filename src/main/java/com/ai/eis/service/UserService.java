package com.ai.eis.service;

import java.util.List;

import com.ai.eis.model.User;

public interface UserService {

    int addUser(User user);

    List<User> findAllUser(int pageNum, int pageSize);
    
    User selectByPrimaryKey(Integer userid);
}
