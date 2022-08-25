package com.example.fsoft_shopee_nhom02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class FSoftShopeeNhom02Application {

    public static void main(String[] args) {
        SpringApplication.run(FSoftShopeeNhom02Application.class, args);
    }
}
