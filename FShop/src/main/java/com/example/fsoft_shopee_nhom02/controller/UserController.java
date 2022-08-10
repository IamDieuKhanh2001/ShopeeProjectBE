package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.auth.ApplicationUser;
import com.example.fsoft_shopee_nhom02.config.GlobalVariable;
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
import java.text.ParseException;
import java.util.Map;


@RestController
@RequestMapping("")
public class UserController {
    @Autowired
    UserService userService;

    //API cần header: Authorization
    @GetMapping("/admin/users")
    ResponseEntity<?> getAllUser() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @GetMapping("/admin/users/infoUser")
    ResponseEntity<?> findByEmailUser(@RequestParam(value = "email", required = false) String email) {
        return ResponseEntity.ok(userService.findByEmailUser(email));
    }

    @GetMapping("/admin/users/searchName")
    ResponseEntity<?> findByName(@RequestParam(value = "name", required = false) String name){
        if (name.isEmpty())
            return ResponseEntity.ok(userService.getAllUser());
        return ResponseEntity.ok(userService.findByName(name));
    }

    @PostMapping("/admin/users/role")
    ResponseEntity<?> upRoleUser(@RequestParam(value = "email", required = false) String email){
        String username = userService.findByEmailUser(email).getUsername();
        if (userService.upRole(email)){
            return ResponseEntity.ok("Update Role User "+username+" Success");
        }
        return ResponseEntity.ok("User "+username+" already have this role!!!");
    }

    @DeleteMapping("/admin/users/role")
    ResponseEntity<?> removeRoleUser(@RequestParam(value = "email", required = false) String email){
        String username = userService.findByEmailUser(email).getUsername();
        if (userService.removeRole(email)){
            return ResponseEntity.ok("Remove User "+username+"'s ROLE_ADMIN Success");
        }
        return ResponseEntity.ok("User "+username+" is having basic user role!!!");
    }
    @DeleteMapping("/admin/users")
    ResponseEntity<?> delete(@RequestParam(value = "email", required = false) String email){
        userService.deleteUser(email);
        return ResponseEntity.ok("Delete Success");
    }

    @GetMapping("/user/myProfile")
    ResponseEntity<?> profileUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((ApplicationUser)principal).getUsername();
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    @DeleteMapping("/user/delete")
    ResponseEntity<?> deleteUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((ApplicationUser)principal).getUsername();
        userService.deleteUser(username);
        return ResponseEntity.ok("Delete Success");
    }

    @PutMapping("/user/changeProfile")
    public SuccessResponseDTO changeProfile(@RequestBody UserDTO userProfileChange) throws ParseException {
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

