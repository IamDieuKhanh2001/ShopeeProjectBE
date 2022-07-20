package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.dto.ResponseObject;
import com.example.fsoft_shopee_nhom02.dto.UserDTO;
import com.example.fsoft_shopee_nhom02.exception.ResourceNotFoundException;
import com.example.fsoft_shopee_nhom02.model.UserEntity;
import com.example.fsoft_shopee_nhom02.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserRepository repository;

    @GetMapping("/admin")
    ResponseEntity<ResponseObject> getAllUser(){
        List<UserEntity> allUsers = repository.findAll();
        if (allUsers.size()>0) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("SUCCESS", "Query user successfully", allUsers));
        }else {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("SUCCESS", "Query user successfully", "Empty"));
        }
    }

    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> findByIDUser(@PathVariable Long id){
        Optional<UserEntity> foundUser =repository.findById(id);
        return foundUser.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("SUCCESS", "Query user successfully", foundUser)
                ):
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("FAILED", "Cannot find user with id = "+id, "")
                );
    }

    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateProduct(@RequestBody UserDTO newUser, @PathVariable Long id) {
        UserEntity updatedProduct = repository.findById(id)
                .map(user -> {
                    user.setAvatar(newUser.getAvatar());
                    user.setDob(newUser.getDob());
                    user.setEmail(newUser.getEmail());
                    user.setGender(newUser.getGender());
                    user.setName(newUser.getName());
                    user.setPassword(newUser.getPassword());
                    user.setPhone(newUser.getPhone());
                    user.setUsername(newUser.getUsername());
                    return repository.save(user);
                }).orElseThrow(() -> new ResourceNotFoundException("Cannot found user with id = " + id));
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("SUCCESS", "Update user successfully", updatedProduct)
        );
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteUser(@PathVariable Long id) {
        boolean exists = repository.existsById(id);
        if(exists) {
            repository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("SUCCESS", "Delete user successfully", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("FAILED", "Cannot find user to delete", "")
        );
    }
}

