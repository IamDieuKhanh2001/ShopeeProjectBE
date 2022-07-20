package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.dto.UserDTO;
import com.example.fsoft_shopee_nhom02.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/admin")
    ResponseEntity<?> getAllUser() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @GetMapping("/{id}")
    ResponseEntity<?> findByIDUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findByIdUser(id));
    }

    @PutMapping("/{id}")
    ResponseEntity<?> updateProduct(@RequestBody UserDTO newUser, @PathVariable Long id) {
        return ResponseEntity.ok(userService.updateUser(newUser, id));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Delete Success");
    }

    @GetMapping("/count")
    ResponseEntity<?> countAllUser(){
        if(userService.countAllUser() == 0L){
            return ResponseEntity.ok("Empty!!");
        }
        return ResponseEntity.ok("Total user: "+userService.countAllUser());
    }
}

