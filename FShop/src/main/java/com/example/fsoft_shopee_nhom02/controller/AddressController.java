package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.auth.ApplicationUserService;
import com.example.fsoft_shopee_nhom02.dto.AddressDTO;
import com.example.fsoft_shopee_nhom02.dto.SuccessResponseDTO;
import com.example.fsoft_shopee_nhom02.exception.ErrorResponse;
import com.example.fsoft_shopee_nhom02.model.AddressEntity;
import com.example.fsoft_shopee_nhom02.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("/address")
    public List<AddressEntity> getAllUserAddressByUsername() {
        String usernameLoggedIn = ApplicationUserService.GetUsernameLoggedIn();
        List<AddressEntity> userAddressList = addressService.getAllUserAddressByUsername(usernameLoggedIn);
        return userAddressList;
    }

    @PostMapping("/address")
    public ResponseEntity<?> createUserAddress(@RequestBody AddressDTO addressDTO) {
        String usernameLoggedIn = ApplicationUserService.GetUsernameLoggedIn();
        if(addressService.saveUserAddress(addressDTO, usernameLoggedIn)) {
            return ResponseEntity.ok(
                    new SuccessResponseDTO(
                    HttpStatus.OK,
                    "Add address for user " + usernameLoggedIn + " success"
                    )
            );
        } else {
            return ResponseEntity.badRequest().body("Delete address for user fail");
        }

    }

    @DeleteMapping("/address/{id}")
    public ResponseEntity<?> deleteUserAddress(@PathVariable long id) {
        if(addressService.deleteAddressByAddressId(id)) {
            return ResponseEntity.ok(
                    new SuccessResponseDTO(HttpStatus.OK, "Delete address for user success")
            );
        }
        return ResponseEntity.badRequest().body("Delete address for user fail");
    }

    @PutMapping("/address/{id}")
    public ResponseEntity<?> updateUserAddress(@RequestBody AddressDTO addressDTO, @PathVariable long id) {

        if(addressService.updateAddress(id, addressDTO)) {
            return ResponseEntity.ok(
                    new SuccessResponseDTO(HttpStatus.OK, "Update address for user success")
            );
        }
        return ResponseEntity.badRequest().body("Update address for user fail");
    }

    @PutMapping("/address/setDefault/{id}")
    public ResponseEntity<?> setDefaultUserAddress(@PathVariable long id) {
        if(addressService.setDefaultAddress(id)) {
            return ResponseEntity.ok(
                    new SuccessResponseDTO(HttpStatus.OK, "Set default address for user success")
            );
        }
        return ResponseEntity.badRequest().body("Set default address for user fail");
    }
}
