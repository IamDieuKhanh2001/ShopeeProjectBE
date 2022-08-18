package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.dto.ShopDTO;
import com.example.fsoft_shopee_nhom02.dto.SuccessResponseDTO;
import com.example.fsoft_shopee_nhom02.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
public class ShopController {

    @Autowired
    private ShopService shopService;

    @PostMapping("/admin/shop")
    public ResponseEntity<?> createNewShop(@RequestBody ShopDTO model){
        return ResponseEntity.ok(shopService.save(model));
    }

    @GetMapping("/shop/get-all")
    public ResponseEntity<?> getAllShop(){
        return ResponseEntity.ok(shopService.findAllShop());
    }

    @GetMapping("/shop/{id}")
    public ResponseEntity<?> getShopById(@PathVariable long id){
        return ResponseEntity.ok(shopService.findShopById(id));
    }

    @GetMapping("/shop/count")
    public ResponseEntity<?> countAllShop(){
        return ResponseEntity.ok(shopService.countAllShop());
    }

    @GetMapping("/shop/search")
    public ResponseEntity<?> findByName(@RequestParam(required = false, defaultValue = "") String keyword){
        return ResponseEntity.ok(shopService.searchByName(keyword));
    }

    @PutMapping("/admin/shop/{id}")
    public ResponseEntity<?> update(@RequestBody ShopDTO model, @PathVariable long id){
        model.setId(id);
        return ResponseEntity.ok(shopService.save(model));
    }

    @DeleteMapping("/admin/shop/{id}")
    public SuccessResponseDTO delete(@PathVariable long id) throws ParseException {
        shopService.delete(id);
        return new SuccessResponseDTO(HttpStatus.OK,"Delete success");
    }
}
