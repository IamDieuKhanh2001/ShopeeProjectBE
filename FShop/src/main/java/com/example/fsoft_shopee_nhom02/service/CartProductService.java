package com.example.fsoft_shopee_nhom02.service;

import com.example.fsoft_shopee_nhom02.dto.CartProductDTO;
import com.example.fsoft_shopee_nhom02.model.CartProductEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartProductService {
        List<CartProductEntity> addCartbyCartIdAndProductId(long productId, long userId, long qty) throws Exception;

         void update(CartProductDTO cartProductDTO) ;

         void delete(long productId , long cartId);

    void updateQtyByCartId(long cartId, int qty, double price) throws Exception;
        List<CartProductEntity> getCartByUserId(long cartId);
        List<CartProductEntity> removeCartByUserId(long cartProductId,long cartId);
        List<CartProductEntity> removeAllCartByUserId(long cartId);
        Boolean checkTotalAmountAgainstCart(double totalAmount,long cartId);
        List<CartProductEntity> getAllCheckoutByUserId(long cartId);

    // List<CartProductDTO> saveProductsForCheckout(List<CheckoutCart> tmp)  throws Exception;

    }

