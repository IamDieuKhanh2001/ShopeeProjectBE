package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.auth.ApplicationUser;
import com.example.fsoft_shopee_nhom02.auth.ApplicationUserService;
import com.example.fsoft_shopee_nhom02.auth.JwtUtil;
import com.example.fsoft_shopee_nhom02.dto.AuthenticationRequest;
import com.example.fsoft_shopee_nhom02.dto.AuthenticationResponse;
import com.example.fsoft_shopee_nhom02.dto.UserDTO;
import com.example.fsoft_shopee_nhom02.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ApplicationUserService applicationUserService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<String> saveUser(@RequestBody UserDTO user) throws Exception {
        applicationUserService.save(user);
        return new ResponseEntity<>(user.getUsername() + " Have been register successful!", HttpStatus. CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException exception) {
            throw  new IllegalStateException("Username or password is invalid");
        }
        final UserDetails userDetails = applicationUserService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        System.out.println();
        return ResponseEntity
                .ok(new AuthenticationResponse(jwt,
                        authenticationRequest.getUsername(),
                        (Set<GrantedAuthority>) userDetails.getAuthorities()));
    }
    //Test api sẽ xóa sau
    @GetMapping("/test")
    public String test() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof ApplicationUser) {
            username = ((ApplicationUser)principal).getUsername();
        } else {
            username = principal.toString();
        }
        String sReturn = "<h1>Login user: " + username + "</h1>";
        return sReturn;
    }
    //Test api sẽ xóa sau
    @GetMapping("/user")
    public String user() {
        String sReturn = "<h1>Welcome User</h1> <h1>Login user:  </h1>";
        return sReturn;
    }
    //Test api sẽ xóa sau
    @GetMapping("/admin")
    public ResponseEntity<?> admin() {
        String sReturn = "<h1>Welcome Admin</h1> <h1>Login user: </h1>";
        return ResponseEntity.ok(sReturn);
    }
}
