package com.anucodes.chatapplication.services;

import com.anucodes.chatapplication.entities.UserInfo;
import com.anucodes.chatapplication.entities.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails extends UserInfo implements UserDetails {

    private final String username;
    private final String password;
    private Collection<?extends GrantedAuthority> authorities;

    public CustomUserDetails(UserInfo userInfo) {
        this.username = userInfo.getUsername();
        this.password = userInfo.getPassword();
        List<GrantedAuthority> auths = new ArrayList<>();

        for(UserRole role: userInfo.getRoles()){
            auths.add(new SimpleGrantedAuthority(role.getName().toUpperCase()));
        }
        this.authorities = auths;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

}
