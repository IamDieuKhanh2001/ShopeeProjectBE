package com.example.fsoft_shopee_nhom02.service.impl;


import com.example.fsoft_shopee_nhom02.model.CartEntity;
import com.example.fsoft_shopee_nhom02.model.CartProductEntity;
import com.example.fsoft_shopee_nhom02.model.ProductEntity;
import com.example.fsoft_shopee_nhom02.repository.CartProductRepository;
import com.example.fsoft_shopee_nhom02.service.CartProductService;
import com.example.fsoft_shopee_nhom02.service.CartService;
import com.example.fsoft_shopee_nhom02.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartProductServiceImpl implements CartProductService {

    @Autowired
    CartProductRepository addCartRepo;

    @Autowired
    ProductService proServices;
    @Autowired
    CartService cartService;


    @Override
    public List<CartProductEntity> addCartbyCartIdAndProductId(long productId, long userId, long qty) throws Exception {
        try {
            if(addCartRepo.getCartByProductIdAnduserId(userId, productId).isPresent()){
                throw new Exception("Product is already exist.");
            }
            CartProductEntity obj = new CartProductEntity();
            obj.setQuantity(qty);
            CartEntity cart = cartService.getCartsById(userId);
            obj.setCartEntity(cart);
            ProductEntity pro = proServices.getProductsById(productId);
            obj.setProductEntity(pro);
            //TODO price has to check with qty
            addCartRepo.save(obj);
            return this.getCartByUserId(userId);
        }catch(Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }

    }

    @Override
    public void updateQtyByCartId(long cartId, int qty, double price) throws Exception {

    }

    @Override
    public List<CartProductEntity> getCartByUserId(long cartId) {
        return null;
    }

    @Override
    public List<CartProductEntity> removeCartByUserId(long cartProductId, long cartId) {
        return null;
    }

    @Override
    public List<CartProductEntity> removeAllCartByUserId(long cartId) {
        return null;
    }

    @Override
    public Boolean checkTotalAmountAgainstCart(double totalAmount, long cartId) {
        return null;
    }

    @Override
    public List<CartProductEntity> getAllCheckoutByUserId(long cartId) {
        return null;
    }
}
