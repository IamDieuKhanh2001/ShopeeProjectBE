package com.example.fsoft_shopee_nhom02.oauth;

import com.example.fsoft_shopee_nhom02.auth.JwtUtil;
import com.example.fsoft_shopee_nhom02.model.RoleEntity;
import com.example.fsoft_shopee_nhom02.model.UserEntity;
import com.example.fsoft_shopee_nhom02.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Component
public class GoogleLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        GoogleOauth2User googleUser = (GoogleOauth2User) authentication.getPrincipal();
        String emailUsername = googleUser.getEmail();
        System.out.println(emailUsername);
        System.out.println(googleUser.getAvatarPicture());

        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(emailUsername);
//        if(!userEntityOptional.isPresent()) {
//            UserEntity userGoogleNew = new UserEntity();
//
//            Optional<RoleEntity> roleUserOptional = roleRepository.findById(Long.parseLong("2")); //Lấy ROLE_USER
//            List<RoleEntity> roleUserList = new ArrayList<>();
//            roleUserOptional.ifPresent(roleUserList::add);
//            Set<RoleEntity> roleUserSet = new HashSet<>(roleUserList);//ép kiểu role thành set gán cho entity user
//        }

        super.onAuthenticationSuccess(request,response,authentication);
    }
}
