package com.example.fsoft_shopee_nhom02.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        Cloudinary cloudinary = null;
        Map config = new HashMap();
        config.put("cloud_name", "dpxgtmzld");
        config.put("api_key", "388178861795212");
        config.put("api_secret", "F-pUcToMhk18nnVmqLN5W8HeViw");
        cloudinary = new Cloudinary(config);
        return cloudinary;
    }
}
