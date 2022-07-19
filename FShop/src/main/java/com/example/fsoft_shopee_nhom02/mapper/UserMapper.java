package com.example.fsoft_shopee_nhom02.mapper;

import com.example.fsoft_shopee_nhom02.dto.UserDTO;
import com.example.fsoft_shopee_nhom02.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserMapper {

    public static UserEntity toEntity (UserDTO userDTO){
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userDTO.getUsername());
        userEntity.setPassword(userDTO.getPassword());
        userEntity.setPhone(userDTO.getPhone());
        userEntity.setName(userDTO.getName());
        userEntity.setGender(userDTO.getGender());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setDob(userDTO.getDob());
        userEntity.setAvatar(userDTO.getAvatar());
        userEntity.setCartEntity(null);
        return userEntity;
    }
}
