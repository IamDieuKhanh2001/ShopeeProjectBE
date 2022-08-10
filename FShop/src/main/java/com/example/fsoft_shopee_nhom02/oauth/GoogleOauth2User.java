package com.example.fsoft_shopee_nhom02.oauth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class GoogleOauth2User implements OAuth2User {

    private OAuth2User oAuth2User;

    public GoogleOauth2User(OAuth2User oAuth2User) {
        this.oAuth2User = oAuth2User;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public String getName() {
        return oAuth2User.getName();
    }

    public String getFullName() {
        return (String) oAuth2User.getAttributes().get("name");
    }

    public String getEmail() {
        for (Object item: this.getAttributes().keySet()) {
            System.out.println((String) item);
        }
        return (String) oAuth2User.getAttributes().get("email");
    }

    public String getAvatarPicture() {
        return (String) oAuth2User.getAttributes().get("picture");
    }

    public String getGivenName() {
        return (String) oAuth2User.getAttributes().get("given_name");
    }

    public String getFamilyName() {
        return (String) oAuth2User.getAttributes().get("family_name");
    }

    public String getSub() {
        return (String) oAuth2User.getAttributes().get("family_name");
    }

}
