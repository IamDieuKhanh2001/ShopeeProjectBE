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
import org.apache.http.client.utils.URIBuilder;
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
import java.net.URISyntaxException;
import java.net.URL;
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
    public ModelAndView home(@RequestParam(value = "jwt") String jwtToken) throws URISyntaxException {
        String username = jwtTokenUtil.extractUsername(jwtToken);
        final UserDetails userDetails = applicationUserService
                .loadUserByUsername(username);
        UserEntity user = userService.findFirstByUsername(userDetails.getUsername());

            URIBuilder url = new URIBuilder("/login");
            url.addParameter("jwt", jwtToken);
            url.addParameter("userId", user.getId().toString());
            url.addParameter("username", userDetails.getUsername());
        String urlString = url.build().toString();
        return new ModelAndView("redirect:https://react02-group06.vercel.app" + urlString);
    }
}
