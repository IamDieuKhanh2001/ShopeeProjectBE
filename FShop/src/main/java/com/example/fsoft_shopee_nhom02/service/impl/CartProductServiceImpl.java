package com.example.fsoft_shopee_nhom02.service.impl;


import com.example.fsoft_shopee_nhom02.dto.CartDetailDTO;
import com.example.fsoft_shopee_nhom02.dto.CartProductDTO;
import com.example.fsoft_shopee_nhom02.exception.NotFoundException;
import com.example.fsoft_shopee_nhom02.mapper.CartProductMapper;
import com.example.fsoft_shopee_nhom02.model.*;
import com.example.fsoft_shopee_nhom02.repository.CartProductRepository;
import com.example.fsoft_shopee_nhom02.repository.CartRepository;
import com.example.fsoft_shopee_nhom02.repository.ProductRepository;
import com.example.fsoft_shopee_nhom02.repository.TypeRepository;
import com.example.fsoft_shopee_nhom02.service.CartProductService;
import com.example.fsoft_shopee_nhom02.service.CartService;
import com.example.fsoft_shopee_nhom02.service.ProductService;
import com.example.fsoft_shopee_nhom02.service.UserService;
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

    @Autowired
    UserService userService;

    UserEntity user;

    @Override
    public CartProductDTO addCart(CartProductDTO cartProductDTO) throws Exception {
        long userId = cartProductDTO.getUserId();
        user = userService.findByIdUser(userId);
        long cartId = user.getCartEntity().getId();
        long qty = cartProductDTO.getQuantity();
        long productId = cartProductDTO.getProductId();
        String type = cartProductDTO.getType();
        CartProductMapper cartProductMapper = new CartProductMapper();

        if (cartProductRepository.getCartByProductIdAndCartId(cartId, productId, type).isPresent()) {
            CartProductEntity obj1 = cartProductRepository.getCartByProductIdAndCartId(cartId, productId, type).
                    orElseThrow(() -> new NotFoundException("lỗi"));
            obj1.setQuantity(obj1.getQuantity() + qty);
            cartProductRepository.save(obj1);
            return cartProductMapper.toCartProductDto(obj1);

        } else {
            CartProductEntity obj = new CartProductEntity();
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
        long userId = cartProductDTO.getUserId();
        user = userService.findByIdUser(userId);
        long cartId = user.getCartEntity().getId();
        CartProductEntity obj =
                cartProductRepository.getCartByProductIdAnduserId(cartId
                        , cartProductDTO.getProductId(), cartProductDTO.getType());
        obj.setQuantity(cartProductDTO.getQuantity());
        cartProductRepository.save(obj);
        CartProductMapper cartProductMapper = new CartProductMapper();

        return cartProductMapper.toCartProductDto(obj);
    }

    @Override
    public void delete(CartProductDTO cartProductDTO) {
        long userId = cartProductDTO.getUserId();
        user = userService.findByIdUser(userId);
        long cartId = user.getCartEntity().getId();
        Long productId = cartProductDTO.getProductId();
        String type = cartProductDTO.getType();
        cartProductRepository.deleteProduct(productId, cartId, type);
    }

    @Override
    public void deleteListOfCartProduct(Collection<Long> productEntity_id, Long cartEntity_id, Collection<String> type) {
        cartProductRepository.removeAllByProductEntityIdInAndCartEntityIdAndTypeIn(productEntity_id, cartEntity_id, type);
    }

    @Override
    public List<CartDetailDTO> getAllCart(long userId) {
        user = userService.findByIdUser(userId);
        Long cartId = user.getCartEntity().getId();
        List<CartProductEntity> cartProductEntities = cartProductRepository.getCart(94L);
        List<CartDetailDTO> cartDetailDTOS = new ArrayList<>();

        if (cartProductEntities.isEmpty()) {
            return cartDetailDTOS;
        }
        for (CartProductEntity cartProductEntity : cartProductEntities) {
            CartDetailDTO cartDetailDTO = new CartDetailDTO();

            cartDetailDTO.setQuantity(cartProductEntity.getQuantity());

            ProductEntity product = productRepository.findById(cartProductEntity.getProductEntity().getId())
                    .orElseThrow(() -> new NotFoundException("not found product"));
            //set name and image
            cartDetailDTO.setName(product.getName());
            cartDetailDTO.setImage(product.getImageProduct());
            cartDetailDTO.setProductId(product.getId());

            //get type
            TypeEntity type = typeRepository.findTypeEntityByProduct(cartProductEntity.getProductEntity().getId(),
                            cartProductEntity.getType())
                    .orElseThrow(() -> new NotFoundException("type and productId transfer" +
                            " not match in Type table in DB"));
            cartDetailDTO.setType(type.getType());
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
