package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.dto.AddressDTO;
import com.example.fsoft_shopee_nhom02.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("/user/{userId}")
    public List<AddressDTO> getAllUserAddressByUserID(@PathVariable long userId) {
        List<AddressDTO> list = addressService.getAllUserAddressByUserId(userId);
        return list;
    }
}
