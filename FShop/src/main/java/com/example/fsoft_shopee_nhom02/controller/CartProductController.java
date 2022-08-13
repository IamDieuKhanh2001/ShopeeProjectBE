package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.auth.ApplicationUserService;
import com.example.fsoft_shopee_nhom02.auth.JwtUtil;
import com.example.fsoft_shopee_nhom02.dto.CartDetailDTO;
import com.example.fsoft_shopee_nhom02.dto.CartProductDTO;
import com.example.fsoft_shopee_nhom02.model.CartProductEntity;
import com.example.fsoft_shopee_nhom02.model.UserEntity;
import com.example.fsoft_shopee_nhom02.service.UserService;
import com.example.fsoft_shopee_nhom02.service.impl.CartProductServiceImpl;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartProductController {

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    CartProductServiceImpl cartProductService;

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationUserService applicationUserService;

    String takeUsernameByAuth(String AUTHORIZATION){
        String jwt =  AUTHORIZATION.substring(7, AUTHORIZATION.length());
        return jwtTokenUtil.extractUsername(jwt);
    }
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken, @RequestBody CartProductDTO cartProductDTO) {
        try {
            String username = takeUsernameByAuth(jwtToken);
            final UserDetails userDetails = applicationUserService
                    .loadUserByUsername(username);
            UserEntity user = userService.findFirstByUsername(userDetails.getUsername());
            Long userId = user.getId();
            cartProductDTO.setUserId(userId);
            return ResponseEntity.ok(cartProductService.addCart(cartProductDTO));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("update")
    public ResponseEntity<?> updateProductCount(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken, @RequestBody CartProductDTO cartProductDTO) {
        String username = takeUsernameByAuth(jwtToken);
        final UserDetails userDetails = applicationUserService
                .loadUserByUsername(username);
        UserEntity user = userService.findFirstByUsername(userDetails.getUsername());
        Long userId = user.getId();
        cartProductDTO.setUserId(userId);
        return ResponseEntity.ok(cartProductService.update(cartProductDTO));
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteProduct(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken, @RequestBody CartProductDTO cartProductDTO) {
        String username = takeUsernameByAuth(jwtToken);
        final UserDetails userDetails = applicationUserService
                .loadUserByUsername(username);
        UserEntity user = userService.findFirstByUsername(userDetails.getUsername());
        Long userId = user.getId();
        cartProductDTO.setUserId(userId);
        try {
            cartProductService.delete(cartProductDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(cartProductDTO);
    }

    @GetMapping("")
    public ResponseEntity<?> getCart(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken) {
        String username = takeUsernameByAuth(jwtToken);
        final UserDetails userDetails = applicationUserService
                .loadUserByUsername(username);
        UserEntity user = userService.findFirstByUsername(userDetails.getUsername());
        Long userId = user.getId();
        return ResponseEntity.ok(cartProductService.getAllCart(userId));
    }
}


