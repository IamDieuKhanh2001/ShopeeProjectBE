package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.dto.CartDetailDTO;
import com.example.fsoft_shopee_nhom02.dto.CartProductDTO;
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
    public ResponseEntity<?> add(@RequestBody CartProductDTO cartProductDTO) {
        try {
            return  ResponseEntity.ok(cartProductService.addCart(cartProductDTO));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("update")
    public ResponseEntity<?>  updateProductCount(@RequestBody CartProductDTO cartProductDTO)
    {

        return  ResponseEntity.ok(cartProductService.update(cartProductDTO));
    }

    @DeleteMapping("")
    public ResponseEntity<?>  deleteProduct(@RequestBody CartProductDTO cartProductDTO)
    {
        try {
            cartProductService.delete(cartProductDTO);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("xoá thành công");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?>getCart(@PathVariable long userId){
        return ResponseEntity.ok(cartProductService.getAllCart(userId));
    }
    @GetMapping("/test")

    public  String test(){
        return "hello";
    }
}

