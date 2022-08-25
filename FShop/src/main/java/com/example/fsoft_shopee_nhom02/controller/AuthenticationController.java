package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.auth.ApplicationUserService;
import com.example.fsoft_shopee_nhom02.auth.JwtUtil;
import com.example.fsoft_shopee_nhom02.config.EmailTemplate;
import com.example.fsoft_shopee_nhom02.config.GlobalVariable;
import com.example.fsoft_shopee_nhom02.dto.*;
import com.example.fsoft_shopee_nhom02.exception.BadRequest;
import com.example.fsoft_shopee_nhom02.model.UserEntity;
import com.example.fsoft_shopee_nhom02.repository.UserRepository;
import com.example.fsoft_shopee_nhom02.service.EmailSenderService;
import com.example.fsoft_shopee_nhom02.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ApplicationUserService applicationUserService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> saveUser(@RequestBody UserDTO user) {
        ResponseEntity<?> saveResult = applicationUserService.save(user);
        if(saveResult.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(new SuccessResponseDTO(HttpStatus.OK,
                    "Tạo tài khoản user " + user.getUsername() + " thành công"));
        } else {
            return ResponseEntity.badRequest().body(saveResult.getBody());
        }
    }

    @GetMapping(path = "/checkEmail/getOtp")
    public OtpSendMailResponseDTO sendOtpCheckToUserEmail(@RequestParam Map<String, String> requestParams) {
        if(requestParams.get("email") == null) {
            throw new BadRequest("Check mail by OTP for user fail! need ?email= param");
        }
        if(requestParams.get("username") == null) {
            throw new BadRequest("Check mail by OTP for user fail! need ?username= param");
        }
        String emailRegister = (requestParams.get("email"));
        String username = (requestParams.get("username"));
        String checkValidEmailOTP = userService.getCheckValidEmailOTP(username, emailRegister);
        return new OtpSendMailResponseDTO("Valid", checkValidEmailOTP);
    }

    @PostMapping("/login")
    public Object authenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
        try {
            authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken
                                    (authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException exception) {
            return ResponseEntity.badRequest().body("Username or password is invalid");
        }
        final UserDetails userDetails = applicationUserService
                .loadUserByUsername(authenticationRequest.getUsername());
        UserEntity user = userService.findFirstByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity
                .ok(new AuthenticationResponse(jwt,
                        user.getId(),
                        authenticationRequest.getUsername(),
                        (Set<GrantedAuthority>) userDetails.getAuthorities()));
    }

    @GetMapping("/")
    public ModelAndView home(HttpServletResponse response, @RequestParam(value = "jwt") String jwtToken, OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        String username = jwtTokenUtil.extractUsername(jwtToken);
        final UserDetails userDetails = applicationUserService
                .loadUserByUsername(username);
        UserEntity user = userService.findFirstByUsername(userDetails.getUsername());

//        response.setHeader("Location", "http://localhost:3000" + jwtToken);
//        response.setStatus(200);

        return new ModelAndView("redirect:http://localhost:3000/login?jwt=" + jwtToken + "&userId=" + user.getId() +
                "&username=" + userDetails.getUsername());
//        return ResponseEntity
//                .ok(new AuthenticationResponse(jwtToken,
//                        user.getId(),
//                        userDetails.getUsername(),
//                        (Set<GrantedAuthority>) userDetails.getAuthorities()));
    }

//    //Test api sẽ xóa sau
//    @GetMapping("/test")
//    public String test() {
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String username;
//
//        if (principal instanceof ApplicationUser) {
//            username = ((ApplicationUser)principal).getUsername();
//        } else {
//            username = principal.toString();
//        }
//        String sReturn = "<h1>Login user: " + username + "</h1>";
//        return sReturn;
//    }
//    //Test api sẽ xóa sau
//    @GetMapping("/user")
//    public String user() {
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String username;
//
//        if (principal instanceof ApplicationUser) {
//            username = ((ApplicationUser)principal).getUsername();
//        } else {
//            username = principal.toString();
//        }
//        String sReturn = "<h1>Welcome User</h1> <h1>Login user:  </h1>";
//        return sReturn;
//    }
//    //Test api sẽ xóa sau
//    @GetMapping("/admin")
//    public ResponseEntity<?> admin() {
//        String sReturn = "<h1>Welcome Admin</h1> <h1>Login user: </h1>";
//        return ResponseEntity.ok(sReturn);
//    }
}
