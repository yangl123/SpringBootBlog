package com.yangle.service.impl;

import com.yangle.domain.User;
import com.yangle.mapper.DemoMapper;
import com.yangle.service.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by yangle on 2017/9/24.
 */
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private DemoMapper demoMapper;
    @Override
    public User getUsers(int id) {
        return demoMapper.getUserById(id);
    }

    @Override
    public User getUserByName(String name) {
        return demoMapper.getUserByName(name);
    }

    @Override
    public List<Map<String, Object>> getUsers() {
        return demoMapper.getUsers();
    }
}
