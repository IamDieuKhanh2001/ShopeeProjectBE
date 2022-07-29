package com.example.fsoft_shopee_nhom02.controller;

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
    public ResponseEntity<?> addCartwithProduct(@RequestBody HashMap<String, String> addCartRequest) {
        try {
            String keys[] = {"productId", "cartId", "quantity"};
            long productId = Long.parseLong(addCartRequest.get("productId"));
            long cartId = Long.parseLong(addCartRequest.get("cartId"));
            long quantity = Long.parseLong(addCartRequest.get("quantity"));
            List<CartProductEntity> obj = cartProductService.addCartbyCartIdAndProductId(productId, cartId, quantity);
                return ResponseEntity.ok(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("products/{id}")
    public void  updateProductCount(@PathVariable long id,
                                    @RequestBody CartProductDTO cartProductDTO)
    {
        cartProductDTO.setId(id);
        cartProductService.update(cartProductDTO);
    }

    @DeleteMapping("")
    public void  deleteProduct(@RequestParam Long productId,
                               @RequestParam Long cartId)
    {
        cartProductService.delete(productId, cartId);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<?>getCart(@PathVariable Long cartId){
        return ResponseEntity.ok(cartProductService.getAllCart(cartId));
    }
    @GetMapping("/test")

    public  String test(){
        return "hello";
    }
}

