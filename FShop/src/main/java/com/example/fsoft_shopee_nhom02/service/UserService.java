package com.example.fsoft_shopee_nhom02.service;

import com.example.fsoft_shopee_nhom02.dto.UserDTO;
import com.example.fsoft_shopee_nhom02.model.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    List<UserEntity> getAllUser();
    UserEntity findByIdUser(Long id);
    UserEntity updateUser(UserDTO newUser, Long id);
    void deleteUser(Long id);
    long countAllUser();
    UserEntity changeProfile(UserDTO user, String username);
    List<UserEntity> getUsersByEmail(String email);
    UserEntity changeUserPasswordByEmail(String newPassword, String email);
}
