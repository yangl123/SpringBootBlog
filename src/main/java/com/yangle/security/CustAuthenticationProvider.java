package com.yangle.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Created by yangle on 2017/10/9.
 */
@Component
public class CustAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private SnailUserDetailsService userService;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        CustUserDetails userDetials = (CustUserDetails) userService.loadUserByUsername(username);
        CustomWebAuthenticationDetails details = (CustomWebAuthenticationDetails) authentication.getDetails();
        String code=details.getCode();
        String kapacode=details.getKapacode();
        if(!kapacode.equals(code)){

            throw new BadCredentialsException("验证码不正确");
        }
        if(userDetials == null){
            throw new BadCredentialsException("没有这个用户.");
        }

        //加密过程在这里体现
        if (!password.equals(userDetials.getPassword())) {
            throw new BadCredentialsException("密码错误");
        }

        Collection<? extends GrantedAuthority> authorities = userDetials.getAuthorities();
        return new UsernamePasswordAuthenticationToken(userDetials, password, authorities);
    }
    @Override
    public boolean supports(Class<?> arg0) {
        return true;
    }

}