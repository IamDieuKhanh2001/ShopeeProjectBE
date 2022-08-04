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

    @GetMapping("/admin/users/{id}")
    ResponseEntity<?> findByIDUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findByIdUser(id));
    }

    @GetMapping("/admin/users/count")
    ResponseEntity<?> countAllUser(){
        if(userService.countAllUser() == 0L){
            return ResponseEntity.ok("Empty!!");
        }
        return ResponseEntity.ok("Total user: "+userService.countAllUser()+", Men user: "+userService.countMen()+
                ", Women user: "+userService.countWomen()+", Kid user: "+userService.countKid());
    }

    @DeleteMapping("/admin/users/{id}")
    ResponseEntity<?> delete(@PathVariable Long id){
        userService.delete(id);
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
        userProfileChange.setModifiedDate(GlobalVariable.getCurrentDateTime());

        userService.changeProfile(userProfileChange, username);
        return new SuccessResponseDTO(HttpStatus.OK,
                "Thay đổi thông tin tài khoản user " + username + " thành công");
    }
}

