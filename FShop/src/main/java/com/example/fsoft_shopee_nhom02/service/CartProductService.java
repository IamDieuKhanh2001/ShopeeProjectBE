package com.example.fsoft_shopee_nhom02.service;

import com.example.fsoft_shopee_nhom02.dto.CartDetailDTO;
import com.example.fsoft_shopee_nhom02.dto.CartProductDTO;
import com.example.fsoft_shopee_nhom02.model.CartProductEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Service
public interface CartProductService {
    CartProductDTO addCart(CartProductDTO cartProductDTO) throws Exception;

    CartProductDTO update(CartProductDTO cartProductDTO);

    void delete(CartProductDTO cartProductDTO);

    void deleteListOfCartProduct(Collection<Long> productEntity_id, Long cartEntity_id, Collection<String> type);

    List<CartDetailDTO> getAllCart(long cartId);
    //List<HashMap<Object,Object>> getAllCart(long cartId)

    CartProductDTO getCartByCartId(long cartId);


    // List<CartProductDTO> saveProductsForCheckout(List<CheckoutCart> tmp)  throws Exception;

}

