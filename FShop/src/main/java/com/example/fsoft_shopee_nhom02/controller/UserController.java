package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.auth.ApplicationUser;
import com.example.fsoft_shopee_nhom02.dto.SuccessResponseDTO;
import com.example.fsoft_shopee_nhom02.dto.UserDTO;
import com.example.fsoft_shopee_nhom02.service.EmailSenderService;
import com.example.fsoft_shopee_nhom02.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.Map;


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

    //API cần header: Authorization
    @PutMapping("/changeProfile")
    public SuccessResponseDTO changeProfile(@RequestBody UserDTO userProfileChange) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof ApplicationUser) {
            username = ((ApplicationUser)principal).getUsername();
        } else {
            throw new IllegalStateException("Thay đổi thông tin tài khoản user thất bại");
        }
        userService.changeProfile(userProfileChange, username);
        return new SuccessResponseDTO(HttpStatus.OK,
                "Thay đổi thông tin tài khoản user " + username + " thành công");
    }
}

