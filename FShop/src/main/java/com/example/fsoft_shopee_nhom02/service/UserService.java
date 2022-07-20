package com.example.fsoft_shopee_nhom02.service;

import com.example.fsoft_shopee_nhom02.dto.ResponseObject;
import com.example.fsoft_shopee_nhom02.dto.UserDTO;
import com.example.fsoft_shopee_nhom02.exception.ResourceNotFoundException;
import com.example.fsoft_shopee_nhom02.model.UserEntity;
import com.example.fsoft_shopee_nhom02.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public ResponseObject getAllUser(){
        List<UserEntity> allUsers = userRepository.findAll();
        if (allUsers.size()>0) {
            return new ResponseObject("SUCCESS", "Query user successfully", allUsers);
        }else {
            return new ResponseObject("SUCCESS", "Query user successfully", "Empty");
        }
    }

    public ResponseObject findByIdUser(Long id){
        Optional<UserEntity> foundUser =userRepository.findById(id);
        return foundUser.isPresent() ?
                new ResponseObject("SUCCESS", "Query user successfully", foundUser):
                new ResponseObject("FAILED", "Cannot find user with id = "+id, "");
    }

    public ResponseObject updateUser(UserDTO newUser, Long id){
        UserEntity updatedProduct = userRepository.findById(id)
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
        return new ResponseObject("SUCCESS", "Update user successfully", updatedProduct);
    }

    public ResponseObject deleteUser(Long id){
        boolean exists = userRepository.existsById(id);
        if(exists) {
            userRepository.deleteById(id);
            return new ResponseObject("SUCCESS", "Delete user successfully", "");
        }
        return new ResponseObject("FAILED", "Cannot find user with id = "+id+" to delete", "");
    }

}
