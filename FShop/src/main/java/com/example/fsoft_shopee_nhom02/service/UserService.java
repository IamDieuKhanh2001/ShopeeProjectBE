package com.example.fsoft_shopee_nhom02.service;

import com.example.fsoft_shopee_nhom02.dto.UserDTO;
import com.example.fsoft_shopee_nhom02.dto.UserProfileDTO;
import com.example.fsoft_shopee_nhom02.model.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface UserService {

    List<UserProfileDTO> getAllUser();
    UserEntity findByIdUser(Long id);
    UserProfileDTO findByEmailUser(String email);
    void delete(Long id);
    void deleteUser(String email);
    boolean upRole(String email);
    boolean removeRole(String email);
    List<UserDTO> findByName(String name);
    UserEntity changeProfile(UserDTO user, String username);
    List<UserEntity> getUsersByEmail(String email);
    UserEntity changeUserPasswordByEmail(String newPassword, String email);
    UserProfileDTO findByUsername(String username);
    Boolean uploadUserAvatar(MultipartFile avatar, String username);
    UserEntity findFirstByUsername(String username);
}
