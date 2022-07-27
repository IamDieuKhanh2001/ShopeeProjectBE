package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.model.CartProductEntity;
import com.example.fsoft_shopee_nhom02.service.impl.CartProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartProductController {

    @Autowired
    CartProductServiceImpl cartProductService;

    @PostMapping("/add")
    public ResponseEntity<?> addCartwithProduct(@RequestBody HashMap<String, String> addCartRequest) {
        try {
            String keys[] = {"productId", "cartId", "quantity"};
            long productId = Long.parseLong(addCartRequest.get("productId"));
            long cartId = Long.parseLong(addCartRequest.get("cartId"));
            long quantity = Long.parseLong(addCartRequest.get("qty"));
            List<CartProductEntity> obj = cartProductService.addCartbyCartIdAndProductId(productId, cartId, quantity);
            return ResponseEntity.ok(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Lỗi");
        }
    }
    @GetMapping("/test")
    public  String test(@RequestBody HashMap<String, String> addCartRequest){
        return "hello";
    }
}

