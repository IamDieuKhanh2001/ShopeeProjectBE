package com.example.fsoft_shopee_nhom02.oauth;

import com.example.fsoft_shopee_nhom02.auth.AuthenticationProvider;
import com.example.fsoft_shopee_nhom02.config.GlobalVariable;
import com.example.fsoft_shopee_nhom02.model.CartEntity;
import com.example.fsoft_shopee_nhom02.model.RoleEntity;
import com.example.fsoft_shopee_nhom02.model.UserEntity;
import com.example.fsoft_shopee_nhom02.repository.RoleRepository;
import com.example.fsoft_shopee_nhom02.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Service
public class GoogleOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        return new GoogleOauth2User(super.loadUser(userRequest));
    }

    public boolean saveUser(GoogleOauth2User googleUser) {
        String[] splitEmail = googleUser.getEmail().split("@"); //create random username for google account
        String randomUsername = splitEmail[0] + "#" + GlobalVariable.GetRandom4DigitNumber();
        UserEntity googleUserRegister =
                new UserEntity(
                        googleUser.getFullName(),
                        randomUsername,
                        googleUser.getEmail(),
                        googleUser.getAvatarPicture(),
                        AuthenticationProvider.GOOGLE,
                        new Timestamp(System.currentTimeMillis())
                );
        googleUserRegister.setCartEntity(new CartEntity()); //Create cart

        RoleEntity roleUser = roleRepository.getRoleEntityById(Long.parseLong("2")); //Get ROLE_USER
        Set<RoleEntity> roleUserSet = new HashSet<>();//Parse type for role to set for entity user
        roleUserSet.add(roleUser);
        googleUserRegister.setRoleEntitySet(roleUserSet);
        try {
            userRepository.save(googleUserRegister);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
}
