package com.kjd.demoday3.security;

import com.kjd.demoday3.vo.UserInfoVO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class AuthUser extends User {
    private final UserInfoVO userInfo;

    public AuthUser(UserInfoVO userInfo, String password, Collection<? extends GrantedAuthority> authorities) {
        super(userInfo.getUsername(), password, authorities);
        this.userInfo = userInfo;
    }

    public UserInfoVO getUserInfo() {
        return userInfo;
    }
}
