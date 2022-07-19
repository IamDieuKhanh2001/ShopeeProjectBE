package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.dto.ShopDTO;
import com.example.fsoft_shopee_nhom02.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShopController {

    @Autowired
    private ShopService shopService;

    @PostMapping("/shop")
    public ResponseEntity<?> createNewShop(@RequestBody ShopDTO model){
        return ResponseEntity.ok(shopService.save(model));
    }
}
