package com.yangle.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Created by yangle on 2017/10/9.
 */
public class CustUserDetails implements UserDetails {
    private String userNmae;
    private String password;
    private String role;

    public void setUserNmae(String userNmae) {
        this.userNmae = userNmae;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CustUserDetails(String userNmae, String password, String role) {
        this.userNmae = userNmae;
        this.password = password;
        this.role = role;
    }

    private static final long serialVersionUID = -1922135614793714181L;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        boolean flag = false;
        if (flag) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList("");
        }
        StringBuilder commaBuilder = new StringBuilder();
        commaBuilder.append("SUPPER MANAGER");
        commaBuilder.append(",");
        commaBuilder.append("hello");
        commaBuilder.append(",");
        commaBuilder.append("view");
        return AuthorityUtils.commaSeparatedStringToAuthorityList(commaBuilder.toString());
    }
    @Override
    public String getPassword() {
        return this.password;
    }
    @Override
    public String getUsername() {
        return this.userNmae;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }

    public CustUserDetails() {
    }
}