package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.dto.AddressDTO;
import com.example.fsoft_shopee_nhom02.model.AddressEntity;
import com.example.fsoft_shopee_nhom02.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("/user/{userName}")
    public List<AddressEntity> getAllUserAddressByUserID(@PathVariable String userName) {
        List<AddressEntity> userAddressList = addressService.getAllUserAddressByUsername(userName);
        return userAddressList;
    }

    @PostMapping("/user")
    public ResponseEntity<?> createUserAddressByUsername(@RequestBody AddressDTO addressDTO) {
        if(addressService.saveUserAddress(addressDTO)) {
            return new ResponseEntity<>("Add address for user success", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Add address for user fail!!", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/user")
    public ResponseEntity<?> deleteUserAddress(@RequestParam Map<String, String> requestParams) {
        if(requestParams.get("addressId") == null) {
            return new ResponseEntity<>("Delete address for user fail! DELETE need ?addressId= param", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Long addressId = Long.parseLong(requestParams.get("addressId"));
        if(addressService.deleteAddressByAddressId(addressId)) {
            return new ResponseEntity<>("Delete address for user success", HttpStatus.OK);
        }
        return new ResponseEntity<>("Delete address for user fail", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/user")
    public ResponseEntity<?> updateUserAddress(@RequestBody AddressDTO addressDTO, @RequestParam Map<String, String> requestParams) {
        if(requestParams.get("addressId") == null) {
            return new ResponseEntity<>("Update address for user fail! UPDATE need ?addressId= param", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Long addressId = Long.parseLong(requestParams.get("addressId"));

        if(addressService.updateAddress(addressId, addressDTO.getAddress())) {
            return new ResponseEntity<>("Update address for user success", HttpStatus.OK);
        }
        return new ResponseEntity<>("Update address for user fail", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
