package com.example.fsoft_shopee_nhom02.service;

import com.example.fsoft_shopee_nhom02.dto.CartDetailDTO;
import com.example.fsoft_shopee_nhom02.dto.CartProductDTO;
import com.example.fsoft_shopee_nhom02.model.CartProductEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public interface CartProductService {
    List<CartProductEntity> addCartbyCartIdAndProductId(long productId, long userId, String type,long qty) throws Exception;

    void update(CartProductDTO cartProductDTO);

    void delete(long productId, long cartId);

    List<CartDetailDTO> getAllCart(long cartId);
    //List<HashMap<Object,Object>> getAllCart(long cartId)

    List<CartProductEntity> getCartByCartId(long cartId);


    // List<CartProductDTO> saveProductsForCheckout(List<CheckoutCart> tmp)  throws Exception;

}

