package com.example.fsoft_shopee_nhom02.auth;

import com.example.fsoft_shopee_nhom02.model.RoleEntity;
import com.example.fsoft_shopee_nhom02.model.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class ApplicationUser implements UserDetails {

    private String username;
    private String password;
    private Set<GrantedAuthority> authorities;

    public ApplicationUser() {
    }

    public ApplicationUser(UserEntity user) {
        this.username = user.getUsername();
        this.password = user.getPassword();

        //Get user authorities
        Set<RoleEntity> roleEntitySet = user.getRoleEntitySet();
        List<RoleEntity> roleEntityList = new ArrayList<>(roleEntitySet);
        Set<GrantedAuthority> role = new HashSet<>();

        roleEntityList.forEach(item -> {
            role.add(new SimpleGrantedAuthority(item.getRoleName()));
        });
        this.authorities = role;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        System.out.println("SET author: " + authorities);
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
}
