package com.yangle.security;

import com.yangle.domain.User;
import com.yangle.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Created by yangle on 2017/10/9.
 */
@Component
public class SnailUserDetailsService implements UserDetailsService {
    @Autowired
    private IUserService userServiceImpl;
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        User user=userServiceImpl.getUserByName(userName);
        if(user==null){
            return null;
        }
CustUserDetails custUserDetails=new CustUserDetails(user.getName(),user.getPassword(),"");
        return custUserDetails;
    }
}