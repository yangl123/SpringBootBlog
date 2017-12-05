package com.yangle.service.impl;

import com.yangle.domain.User;
import com.yangle.mapper.UserMapper;
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
    private UserMapper userMapper;

    @Override
    public User getUser(String userId) {
        return userMapper.getUserByuserId(userId);
    }
}
