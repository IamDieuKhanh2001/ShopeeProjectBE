package com.example.fsoft_shopee_nhom02.service;

import com.example.fsoft_shopee_nhom02.dto.UserDTO;
import com.example.fsoft_shopee_nhom02.model.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface UserService {

    List<UserDTO> getAllUser();
    UserEntity findByIdUser(Long id);
    UserEntity updateUser(UserDTO newUser, Long id);
    void delete(Long id);
    void deleteUser(String username);
    long countAllUser();
    long countMen();
    long countWomen();
    long countKid();
    UserEntity changeProfile(UserDTO user, String username);
    List<UserEntity> getUsersByEmail(String email);
    UserEntity changeUserPasswordByEmail(String newPassword, String email);
    UserEntity findByUsername(String username);
    Boolean uploadUserAvatar(MultipartFile avatar, String username);
}
