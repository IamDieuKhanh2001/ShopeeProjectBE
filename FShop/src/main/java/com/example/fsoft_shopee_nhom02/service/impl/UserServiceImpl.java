package com.example.fsoft_shopee_nhom02.service.impl;

import com.example.fsoft_shopee_nhom02.dto.UserDTO;
import com.example.fsoft_shopee_nhom02.exception.NotFoundException;
import com.example.fsoft_shopee_nhom02.exception.ResourceNotFoundException;
import com.example.fsoft_shopee_nhom02.model.AddressEntity;
import com.example.fsoft_shopee_nhom02.model.UserEntity;
import com.example.fsoft_shopee_nhom02.repository.UserRepository;
import com.example.fsoft_shopee_nhom02.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserEntity> getAllUser() {
        List<UserEntity> allUsers = userRepository.findAll();
        if (allUsers.size()>0) {
            return allUsers;
        }else {
            throw new NotFoundException("Empty!!");
        }
    }

    @Override
    public UserEntity findByIdUser(Long id) {
        UserEntity foundUser =userRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Not found shop with id = "+id));
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
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public long countAllUser() {
        return userRepository.count();
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
}
