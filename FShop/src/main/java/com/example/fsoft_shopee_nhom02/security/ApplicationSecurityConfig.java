package com.example.fsoft_shopee_nhom02.security;

import com.example.fsoft_shopee_nhom02.auth.ApplicationUserService;
import com.example.fsoft_shopee_nhom02.exception.BadRequest;
//import com.example.fsoft_shopee_nhom02.filters.CORSFilter;
import com.example.fsoft_shopee_nhom02.filters.JwtRequestFilter;
import com.example.fsoft_shopee_nhom02.oauth.GoogleLoginSuccessHandler;
import com.example.fsoft_shopee_nhom02.oauth.GoogleOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import static com.example.fsoft_shopee_nhom02.security.ApplicationUserRole.*;
@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ApplicationUserService applicationUserService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private GoogleOauth2UserService googleOauth2UserService;

    @Autowired
    private GoogleLoginSuccessHandler googleLoginSuccessHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                    .antMatchers("/", "/login", "/register",
                            "/products/**", "/products","/order/**",
                             "/recoveryPassword/**","/category/**",
                            "/category","/shop/**","/shop","/subcategory/**",
                            "/subcategory", "/login/oauth2/code/google", "/oauth2/**","/NotificationService/**").permitAll() //Các API không cần đăng nhập
                    .antMatchers().hasAnyRole(ADMIN.name(), USER.name()) //Các API cần đăng nhập bằng tk admin, user
                    .antMatchers("/admin/**").hasRole(ADMIN.name()) //Các API cần đăng nhập bằng tk admin
                    .antMatchers("/user/**").hasRole(USER.name()) //Các API cần đăng nhập bằng tk user
                .anyRequest()
                    .authenticated() //Các API còn lại cần phải đăng nhập
                .and()
                    .oauth2Login()
                    .userInfoEndpoint()
                    .userService(googleOauth2UserService)
                .and()
                    .successHandler(googleLoginSuccessHandler)
                .and()
                    .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        http.cors();
//        http.addFilterBefore(new CORSFilter(), ChannelProcessingFilter.class);
    }
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUserService);
        return provider;
    }

}
