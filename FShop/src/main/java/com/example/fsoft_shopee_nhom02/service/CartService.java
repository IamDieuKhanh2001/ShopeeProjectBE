package com.example.fsoft_shopee_nhom02.service;

import com.example.fsoft_shopee_nhom02.model.CartEntity;
import com.example.fsoft_shopee_nhom02.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    @Autowired
    CartRepository cartRepository;
    public CartEntity getCartsById(long cartID) throws Exception {
        return  cartRepository.findById(cartID).orElseThrow(() -> new Exception("cart is not found"));
    }
}
