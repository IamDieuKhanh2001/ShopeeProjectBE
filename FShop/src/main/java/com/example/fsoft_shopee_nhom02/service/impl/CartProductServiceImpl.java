package com.example.fsoft_shopee_nhom02.service.impl;


import com.example.fsoft_shopee_nhom02.dto.CartDetailDTO;
import com.example.fsoft_shopee_nhom02.dto.CartProductDTO;
import com.example.fsoft_shopee_nhom02.exception.NotFoundException;
import com.example.fsoft_shopee_nhom02.mapper.CartProductMapper;
import com.example.fsoft_shopee_nhom02.model.CartEntity;
import com.example.fsoft_shopee_nhom02.model.CartProductEntity;
import com.example.fsoft_shopee_nhom02.model.ProductEntity;
import com.example.fsoft_shopee_nhom02.model.TypeEntity;
import com.example.fsoft_shopee_nhom02.repository.CartProductRepository;
import com.example.fsoft_shopee_nhom02.repository.CartRepository;
import com.example.fsoft_shopee_nhom02.repository.ProductRepository;
import com.example.fsoft_shopee_nhom02.repository.TypeRepository;
import com.example.fsoft_shopee_nhom02.service.CartProductService;
import com.example.fsoft_shopee_nhom02.service.CartService;
import com.example.fsoft_shopee_nhom02.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartProductServiceImpl implements CartProductService {

    @Autowired
    CartProductRepository cartProductRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    TypeRepository typeRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService proServices;
    @Autowired
    CartService cartService;


    @Override
    public CartProductDTO addCart(CartProductDTO cartProductDTO) throws Exception {
        long cartId = cartProductDTO.getCartId();
        long qty = cartProductDTO.getQuantity();
        long productId = cartProductDTO.getProductId();
        String type = cartProductDTO.getType();
        CartProductEntity obj = new CartProductEntity();
        CartProductMapper cartProductMapper = new CartProductMapper();
        CartProductEntity obj1 = cartProductRepository.getCartByProductIdAnduserId(cartId, productId, type);
        if (cartProductRepository.getCartByProductIdAndCartId(cartId, productId, type).isPresent()) {
            obj1.setQuantity(obj1.getQuantity() + qty);
            cartProductRepository.save(obj1);
            return cartProductMapper.toCartProductDto(obj1);
        } else {
            obj.setQuantity(qty);
            CartEntity cart = cartService.getCartsById(cartId);
            obj.setCartEntity(cart);
            ProductEntity pro = proServices.getProductsById(productId);
            obj.setProductEntity(pro);
            obj.setType(type);
            cartProductRepository.save(obj);
            return cartProductMapper.toCartProductDto(obj);
        }

    }

    @Override
    public CartProductDTO update(CartProductDTO cartProductDTO) {
        CartProductEntity obj =
                cartProductRepository.getCartByProductIdAnduserId(cartProductDTO.getCartId()
                        , cartProductDTO.getProductId(), cartProductDTO.getType());
        obj.setQuantity(cartProductDTO.getQuantity());
        cartProductRepository.save(obj);
        CartProductMapper cartProductMapper = new CartProductMapper();

        return cartProductMapper.toCartProductDto(obj);
    }

    @Override
    public void delete(CartProductDTO cartProductDTO) {
        Long cartId = cartProductDTO.getCartId();
        Long productId = cartProductDTO.getProductId();
        String type = cartProductDTO.getType();
        cartProductRepository.deleteProduct(productId, cartId, type);
    }

    @Override
    public void deleteListOfCartProduct(Collection<Long> productEntity_id, Long cartEntity_id, Collection<String> type) {
        cartProductRepository.removeAllByProductEntityIdInAndCartEntityIdAndTypeIn(productEntity_id, cartEntity_id, type);
    }

    @Override
    public List<CartDetailDTO> getAllCart(long cartId) {
        List<CartProductEntity> cartProductEntities = cartProductRepository.getCart(cartId);
        List<CartDetailDTO> cartDetailDTOS = new ArrayList<>();
        List<Integer> test = new ArrayList<>();
        for (CartProductEntity cartProduct : cartProductEntities) {
            CartDetailDTO cartDetailDTO = new CartDetailDTO();

            cartDetailDTO.setQuantity(cartProduct.getQuantity());

            ProductEntity product = productRepository.findById(cartProduct.getProductEntity().getId())
                    .orElseThrow(() -> new NotFoundException("not found product"));
            //set name and image
            cartDetailDTO.setName(product.getName());
            cartDetailDTO.setImage(product.getImageProduct());

            //get type
            TypeEntity type = typeRepository.findProductByType(cartProduct.getProductEntity().getId(), cartProduct.getType());
            cartDetailDTO.setType(cartProduct.getType());
            cartDetailDTO.setPrice(type.getPrice());

            cartDetailDTO.setTotalPrice(cartDetailDTO.getTotalPrice());
            cartDetailDTOS.add(cartDetailDTO);
        }
        return cartDetailDTOS;
    }

    @Override
    public CartProductDTO getCartByCartId(long cartId) {
        CartProductMapper cartProductMapper = new CartProductMapper();
        return cartProductMapper.toCartProductDto(cartProductRepository.getCartByCartId(cartId));
    }
}
