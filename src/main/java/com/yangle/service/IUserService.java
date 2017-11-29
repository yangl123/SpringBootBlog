package com.yangle.service;

import com.yangle.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by yangle on 2017/9/24.
 */
@Service
public interface IUserService {
    User getUsers(int id);
    User getUserByName(String name);
    List<Map<String,Object>> getUsers();
}
