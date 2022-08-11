package com.example.fsoft_shopee_nhom02.service.impl;

import com.example.fsoft_shopee_nhom02.dto.UserDTO;
import com.example.fsoft_shopee_nhom02.dto.UserProfileDTO;
import com.example.fsoft_shopee_nhom02.exception.NotFoundException;
import com.example.fsoft_shopee_nhom02.exception.ResourceNotFoundException;
import com.example.fsoft_shopee_nhom02.mapper.UserMapper;
import com.example.fsoft_shopee_nhom02.model.RoleEntity;
import com.example.fsoft_shopee_nhom02.model.UserEntity;
import com.example.fsoft_shopee_nhom02.repository.RoleRepository;
import com.example.fsoft_shopee_nhom02.repository.UserRepository;
import com.example.fsoft_shopee_nhom02.service.CloudinaryService;
import com.example.fsoft_shopee_nhom02.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.*;

@Component
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public List<UserProfileDTO> getAllUser() {
        List<UserProfileDTO> allUserDTO = new ArrayList<>();
        List<UserEntity> allUsers = userRepository.findAll();
        if (allUsers.size()>0) {
            for (UserEntity user : allUsers){
                UserProfileDTO userProfileDTO = UserMapper.toUserProfileDTO(user);
                allUserDTO.add(userProfileDTO);
            }
            return allUserDTO;
        }else {
            throw new NotFoundException("Empty!!");
        }
    }

    @Override
    public UserEntity findByIdUser(Long id) {
        UserEntity foundUser =userRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Not found user with id = "+id));
        return foundUser;
    }

    @Override
    public UserProfileDTO findByEmailUser(String email) {
        UserEntity foundUser =userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("Not found user with email = "+email));
        UserProfileDTO profileDTO = UserMapper.toUserProfileDTO(foundUser);
        return profileDTO;
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void deleteUser(String email) {
        UserEntity deleteUser = userRepository.findByEmail(email).orElseThrow(()
                -> new  NotFoundException("Cannot found user with email = " + email));
        userRepository.deleteById(deleteUser.getId());
    }

    @Override
    public boolean upRole(String email) {
        Optional<UserEntity> changeRoleUser = userRepository.findByEmail(email);
        Optional<RoleEntity> roleUserOptional = roleRepository.findById(Long.parseLong("1"));
        Set<RoleEntity> roleUser = changeRoleUser.get().getRoleEntitySet();
        if (!roleUser.contains(roleUserOptional.get())) {
            roleUser.add(roleUserOptional.get());
            changeRoleUser.get().setRoleEntitySet(roleUser);
            changeRoleUser.get().setModifiedDate(new Timestamp(System.currentTimeMillis()));
            userRepository.save(changeRoleUser.get());
            return true;
        }
        return false;
    }

    @Override
    public boolean removeRole(String email) {
        Optional<RoleEntity> roleUserOptional = roleRepository.findById(Long.parseLong("1"));
        Optional<UserEntity> changeRoleUser = userRepository.findByEmail(email);
        Set<RoleEntity> roleUser = changeRoleUser.get().getRoleEntitySet();
        if (roleUser.contains(roleUserOptional.get())) {
            roleUser.remove(roleUserOptional.get());
            changeRoleUser.get().setRoleEntitySet(roleUser);
            changeRoleUser.get().setModifiedDate(new Timestamp(System.currentTimeMillis()));
            userRepository.save(changeRoleUser.get());
            return true;
        }
        return false;
    }

    @Override
    public List<UserProfileDTO> findByName(String name) {
        List<UserEntity> allUsers = userRepository.findAll();
        List<UserProfileDTO> findName = new ArrayList<>();
        for (UserEntity user : allUsers){
            if (user.getName().toLowerCase().contains(name.toLowerCase())){
                UserProfileDTO userProfileDTO = UserMapper.toUserProfileDTO(user);
                findName.add(userProfileDTO);
            }
        }
        return findName;
    }

    @Override
    public UserEntity changeProfile(UserDTO userChange, String username) {
        UserEntity changeProfileUser = userRepository.findByUsername(username)
                .map(user -> {
                    user.setDob(userChange.getDob());
                    user.setGender(userChange.getGender());
                    user.setName(userChange.getName());
                    user.setModifiedDate(new Timestamp(System.currentTimeMillis()));
                    return userRepository.save(user);
                }).orElseThrow(() -> new ResourceNotFoundException("Cannot found user with username = " + username));
        return changeProfileUser;
    }

    @Override
    public List<UserEntity> getUsersByEmail(String email) {
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(email);
        List<UserEntity> userEntityList = new ArrayList<>();
        if (userEntityOptional.isPresent()) {
            userEntityList.add(userEntityOptional.get());
        } else {
            throw new IllegalStateException("User with this email not found!!");
        }
        return userEntityList;
    }

    @Override
    public UserEntity changeUserPasswordByEmail(String newPassword, String email) {
        System.out.println(newPassword);
        UserEntity userChangePassword = userRepository.findByEmail(email)
                .map(user -> {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    return userRepository.save(user);
                }).orElseThrow(() -> new ResourceNotFoundException("Cannot found user with email = " + email));
        return userChangePassword;
    }

    @Override
    public UserProfileDTO findByUsername(String username) {
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if(!user.isPresent()){
            throw new IllegalStateException("Khong tim thay username "+username);
        }
        UserProfileDTO userProfile = UserMapper.toUserProfileDTO(user.get());
        return userProfile;
    }

    @Override
    public Boolean uploadUserAvatar(MultipartFile avatar, String username) {
        Optional<UserEntity> usersOptional = userRepository.findByUsername(username);
        if(!usersOptional.isPresent()) {
            throw new IllegalStateException("Username " + username + " not found");
        }
        UserEntity userLogin = usersOptional.get();
        String url = cloudinaryService.uploadFile(
                avatar,
                String.valueOf(userLogin.getId()),
                "ShopeeProject" + "/" + "Avatar");
        System.out.println(url);
        if(url == "-1") {
            throw new IllegalStateException("khong upload duoc");
        }
        userLogin.setAvatar(url);
        userLogin.setModifiedDate(new Timestamp(System.currentTimeMillis()));       //Cập nhật thời gian chỉnh sửa
        userRepository.save(userLogin);
        return true;
    }

    @Override
    public UserEntity findFirstByUsername(String username) {
        return userRepository.findFirstByUsername(username);
    }
}
