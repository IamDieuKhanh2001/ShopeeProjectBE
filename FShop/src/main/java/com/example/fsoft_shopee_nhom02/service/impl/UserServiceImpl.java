package com.example.fsoft_shopee_nhom02.service.impl;

import com.example.fsoft_shopee_nhom02.dto.UserDTO;
import com.example.fsoft_shopee_nhom02.exception.NotFoundException;
import com.example.fsoft_shopee_nhom02.exception.ResourceNotFoundException;
import com.example.fsoft_shopee_nhom02.mapper.UserMapper;
import com.example.fsoft_shopee_nhom02.model.UserEntity;
import com.example.fsoft_shopee_nhom02.repository.UserRepository;
import com.example.fsoft_shopee_nhom02.service.CloudinaryService;
import com.example.fsoft_shopee_nhom02.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public List<UserDTO> getAllUser() {
        List<UserDTO> allUserDTO = new ArrayList<>();
        List<UserEntity> allUsers = userRepository.findAll();
        if (allUsers.size()>0) {
            for (UserEntity user : allUsers){
                UserDTO userDTO = UserMapper.toUserDTO(user);
                allUserDTO.add(userDTO);
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
    public UserEntity updateUser(UserDTO newUser, Long id) {
        UserEntity updatedUser = userRepository.findById(id)
                .map(user -> {
                    user.setAvatar(newUser.getAvatar());
                    user.setDob(newUser.getDob());
                    user.setEmail(newUser.getEmail());
                    user.setGender(newUser.getGender());
                    user.setName(newUser.getName());
                    user.setPassword(newUser.getPassword());
                    user.setPhone(newUser.getPhone());
                    user.setUsername(newUser.getUsername());
                    return userRepository.save(user);
                }).orElseThrow(() -> new ResourceNotFoundException("Cannot found user with id = " + id));
        return updatedUser;
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void deleteUser(String username) {
        Optional<UserEntity> deleteUser = userRepository.findByUsername(username);
        userRepository.deleteById(deleteUser.get().getId());
    }

    @Override
    public long countAllUser() {
        return userRepository.count();
    }

    @Override
    public long countMen() {
        List<UserDTO> MenUser = new ArrayList<>();
        List<UserEntity> userEntity = userRepository.findAll();
        for (UserEntity user : userEntity){
            if (user.getGender().equals("Nam")){
                UserDTO userDTO = UserMapper.toUserDTO(user);
                MenUser.add(userDTO);
            }
        }
        return MenUser.size();
    }

    @Override
    public long countWomen() {
        List<UserDTO> WomenUser = new ArrayList<>();
        List<UserEntity> userEntity = userRepository.findAll();
        for (UserEntity user : userEntity){
            if (user.getGender().equals("Nữ")){
                UserDTO userDTO = UserMapper.toUserDTO(user);
                WomenUser.add(userDTO);
            }
        }
        return WomenUser.size();
    }

    @Override
    public long countKid() {
        List<UserDTO> KidUser = new ArrayList<>();
        List<UserEntity> userEntity = userRepository.findAll();
        for (UserEntity user : userEntity){
            UserDTO userDTO = UserMapper.toUserDTO(user);
            if (userDTO.getAge() < 18)
                KidUser.add(userDTO);
        }
        return KidUser.size();
    }

    @Override
    public UserEntity changeProfile(UserDTO userChange, String username) {
        UserEntity changeProfileUser = userRepository.findByUsername(username)
                .map(user -> {
                    user.setDob(userChange.getDob());
                    user.setGender(userChange.getGender());
                    user.setName(userChange.getName());
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
    public UserEntity findByUsername(String username) {
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if(!user.isPresent()){
            throw new IllegalStateException("Khong tim thay username");
        }

        return user.get();
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
        userLogin.setModifiedDate(new Timestamp(System.currentTimeMillis()));
        userRepository.save(userLogin);
        return true;
    }
}
