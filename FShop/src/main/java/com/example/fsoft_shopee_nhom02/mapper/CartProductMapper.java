package com.example.fsoft_shopee_nhom02.mapper;

import com.example.fsoft_shopee_nhom02.dto.CartProductDTO;
import com.example.fsoft_shopee_nhom02.dto.CategoryDTO;
import com.example.fsoft_shopee_nhom02.model.CartProductEntity;
import com.example.fsoft_shopee_nhom02.model.CategoryEntity;

public class CartProductMapper {
    public CartProductMapper() {
    }

    public  CartProductDTO toCartProductDto(CartProductEntity cartProductEntity) {
        CartProductDTO cartProductDTO = new CartProductDTO();
        cartProductDTO.setCartId(cartProductEntity.getCartEntity().getId());
        cartProductDTO.setProductId(cartProductEntity.getProductEntity().getId());
        cartProductDTO.setQuantity(cartProductEntity.getQuantity());
        cartProductDTO.setId(cartProductEntity.getId());
        cartProductDTO.setType(cartProductEntity.getType());
        return cartProductDTO;
    }
}

