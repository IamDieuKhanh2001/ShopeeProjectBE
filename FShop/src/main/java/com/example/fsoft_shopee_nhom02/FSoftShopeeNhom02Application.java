package com.example.fsoft_shopee_nhom02;

import com.example.fsoft_shopee_nhom02.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class FSoftShopeeNhom02Application {

    public static void main(String[] args) {
        SpringApplication.run(FSoftShopeeNhom02Application.class, args);
    }

}
