package com.example.fsoft_shopee_nhom02.oauth;

import com.example.fsoft_shopee_nhom02.auth.AuthenticationProvider;
import com.example.fsoft_shopee_nhom02.auth.JwtUtil;
import com.example.fsoft_shopee_nhom02.model.CartEntity;
import com.example.fsoft_shopee_nhom02.model.RoleEntity;
import com.example.fsoft_shopee_nhom02.model.UserEntity;
import com.example.fsoft_shopee_nhom02.repository.RoleRepository;
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
import java.sql.Timestamp;
import java.util.*;

@Component
public class GoogleLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

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
//        System.out.println(emailUsername);
//        System.out.println(googleUser.getAvatarPicture());
        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(emailUsername);
        if(!userEntityOptional.isPresent()) { //Lần đầu đăng nhap google
            UserEntity googleUserRegister =
                    new UserEntity(
                            googleUser.getName(),
                            emailUsername,
                            googleUser.getEmail(),
                            googleUser.getAvatarPicture(),
                            AuthenticationProvider.GOOGLE,
                            new Timestamp(System.currentTimeMillis())
                            );
            googleUserRegister.setCartEntity(new CartEntity()); //Tạo cart

            RoleEntity roleUser = roleRepository.getRoleEntityById(Long.parseLong("2")); //Lấy ROLE_USER
//            List<RoleEntity> roleUserList = new ArrayList<>();
//            roleUserOptional.ifPresent(roleUserList::add);
            Set<RoleEntity> roleUserSet = new HashSet<>();//ép kiểu role thành set gán cho entity user
            roleUserSet.add(roleUser);
            googleUserRegister.setRoleEntitySet(roleUserSet);
//            userRepository.save(googleUserRegister);
        }
//        else {
//            UserEntity userGoogleRegistered = userRepository.findFirstByUsername(emailUsername);
//
//            userRepository.save(userFacebookRegistered);
//        }


        super.onAuthenticationSuccess(request,response,authentication);
    }
}
