package com.example.fsoft_shopee_nhom02.mapper;

import com.example.fsoft_shopee_nhom02.dto.UserDTO;
import com.example.fsoft_shopee_nhom02.model.UserEntity;

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
        userEntity.setCreatedDate(userDTO.getCreatedDate());
        userEntity.setModifiedDate(userDTO.getModifiedDate());
        userEntity.setCartEntity(null);
        return userEntity;
    }

    public static UserDTO toUserDTO(UserEntity userEntity){
        UserDTO userDTO = new UserDTO();

        userDTO.setId(userEntity.getId());
        userDTO.setUsername(userEntity.getUsername());
        userDTO.setPassword(userEntity.getPassword());
        userDTO.setPhone(userEntity.getPhone());
        userDTO.setName(userEntity.getName());
        userDTO.setGender(userEntity.getGender());
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setDob(userEntity.getDob());
        userDTO.setAvatar(userEntity.getAvatar());
        userDTO.setCreatedDate(userEntity.getCreatedDate());
        userDTO.setModifiedDate(userEntity.getModifiedDate());
        userDTO.setRoleEntitySet(userEntity.getRoleEntitySet());

        return userDTO;
    }
}
