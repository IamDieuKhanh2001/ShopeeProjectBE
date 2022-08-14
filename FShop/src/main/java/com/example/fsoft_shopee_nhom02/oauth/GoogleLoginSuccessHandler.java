package com.example.fsoft_shopee_nhom02.oauth;

import com.example.fsoft_shopee_nhom02.auth.AuthenticationProvider;
import com.example.fsoft_shopee_nhom02.auth.JwtUtil;
import com.example.fsoft_shopee_nhom02.config.GlobalVariable;
import com.example.fsoft_shopee_nhom02.exception.BadRequest;
import com.example.fsoft_shopee_nhom02.model.CartEntity;
import com.example.fsoft_shopee_nhom02.model.RoleEntity;
import com.example.fsoft_shopee_nhom02.model.UserEntity;
import com.example.fsoft_shopee_nhom02.repository.RoleRepository;
import com.example.fsoft_shopee_nhom02.repository.UserRepository;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@Component
@Transactional
public class GoogleLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoogleOauth2UserService googleOauth2UserService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    private String getJwtTokenByUsername(String username) {
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(username);
        return jwtTokenUtil.generateToken(userDetails);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        GoogleOauth2User googleUser = (GoogleOauth2User) authentication.getPrincipal();
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(googleUser.getEmail());
        if(!userEntityOptional.isPresent()) { //Lần đầu đăng nhap google
            if(googleOauth2UserService.saveUser(googleUser)) {
                Optional<UserEntity> userLoginByGoogleOptional = userRepository.findByEmail(googleUser.getEmail());
                String jwtToken = getJwtTokenByUsername(userLoginByGoogleOptional.get().getUsername());
                response.sendRedirect("/?jwt=" + jwtToken);
            }
        } else {
            UserEntity userGoogleRegistered = userEntityOptional.get();
            userGoogleRegistered.setModifiedDate(new Timestamp(System.currentTimeMillis()));
            userGoogleRegistered.setName(googleUser.getFullName());
            userRepository.save(userGoogleRegistered);
            String jwtToken = getJwtTokenByUsername(userGoogleRegistered.getUsername());
            response.sendRedirect("/?jwt=" + jwtToken);
        }
        //Login fail exception

    }
}
