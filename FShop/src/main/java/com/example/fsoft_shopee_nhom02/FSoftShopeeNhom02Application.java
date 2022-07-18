package com.example.fsoft_shopee_nhom02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class FSoftShopeeNhom02Application {

    public static void main(String[] args) {
        SpringApplication.run(FSoftShopeeNhom02Application.class, args);
    }


}
