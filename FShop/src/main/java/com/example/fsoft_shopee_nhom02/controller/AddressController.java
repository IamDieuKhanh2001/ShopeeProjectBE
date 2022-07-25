package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.auth.ApplicationUserService;
import com.example.fsoft_shopee_nhom02.config.GlobalVariable;
import com.example.fsoft_shopee_nhom02.dto.AddressDTO;
import com.example.fsoft_shopee_nhom02.dto.SuccessResponseDTO;
import com.example.fsoft_shopee_nhom02.model.AddressEntity;
import com.example.fsoft_shopee_nhom02.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("/user")
    public List<AddressEntity> getAllUserAddressByUsername() {
        String usernameLoggedIn = ApplicationUserService.GetUsernameLoggedIn();
        List<AddressEntity> userAddressList = addressService.getAllUserAddressByUsername(usernameLoggedIn);
        return userAddressList;
    }

    @PostMapping("/user")
    public SuccessResponseDTO createUserAddress(@RequestBody AddressDTO addressDTO) {
        String usernameLoggedIn = ApplicationUserService.GetUsernameLoggedIn();
        if(addressService.saveUserAddress(addressDTO, usernameLoggedIn)) {
            return new SuccessResponseDTO(
                    HttpStatus.CREATED,
                    "Add address for user " + usernameLoggedIn + " success");
        } else {
            throw new IllegalStateException("Add address for user fail");
        }

    }

    @DeleteMapping("/user")
    public SuccessResponseDTO deleteUserAddress(@RequestParam Map<String, String> requestParams) {
        if(requestParams.get("addressId") == null) {
            throw new IllegalStateException("Delete address for user fail! DELETE need ?addressId= param");
        }
        Long addressId = Long.parseLong(requestParams.get("addressId"));
        if(addressService.deleteAddressByAddressId(addressId)) {
            return new SuccessResponseDTO(HttpStatus.OK, "Delete address for user success");
        }
        throw new IllegalStateException("Delete address for user fail");
    }

    @PutMapping("/user")
    public SuccessResponseDTO updateUserAddress(@RequestBody AddressDTO addressDTO, @RequestParam Map<String, String> requestParams) {
        if(requestParams.get("addressId") == null) {
            throw new IllegalStateException("Update address for user fail! UPDATE need ?addressId= param");
        }
        Long addressId = Long.parseLong(requestParams.get("addressId"));

        if(addressService.updateAddress(addressId, addressDTO)) {
            return new SuccessResponseDTO(HttpStatus.OK, "Update address for user success");
        }
        throw new IllegalStateException("Update address for user fail");
    }
}
