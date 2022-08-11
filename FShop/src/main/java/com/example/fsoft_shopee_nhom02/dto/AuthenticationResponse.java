package com.example.fsoft_shopee_nhom02.dto;

import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

public class AuthenticationResponse {

    private String jwt;
    private Long id;
    private String username;
    private Set<GrantedAuthority> role;

    public Set<GrantedAuthority> getRole() {
        return role;
    }

    public void setRole(Set<GrantedAuthority> role) {
        this.role = role;
    }

    public AuthenticationResponse(String jwt, Long id, String username, Set<GrantedAuthority> role) {
        this.jwt = jwt;
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getJwt() {
        return jwt;
    }
}
