package com.example.fsoft_shopee_nhom02.service.impl;


import com.example.fsoft_shopee_nhom02.dto.CartDetailDTO;
import com.example.fsoft_shopee_nhom02.dto.CartProductDTO;
import com.example.fsoft_shopee_nhom02.exception.NotFoundException;
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
    public List<CartProductEntity> addCartbyCartIdAndProductId(long productId, long cartId,String type, long qty) throws Exception {
        CartProductEntity obj = new CartProductEntity();
        CartProductEntity obj1 = cartProductRepository.getCartByProductIdAnduserId(cartId, productId,type);
        try {
            if(cartProductRepository.getCartByProductIdAndCartId(cartId, productId,type).isPresent()){
                obj1.setQuantity(obj1.getQuantity()+qty);
                cartProductRepository.save(obj1);
            }
            else {
            obj.setQuantity(qty);
            CartEntity cart = cartService.getCartsById(cartId);
            obj.setCartEntity(cart);
            ProductEntity pro = proServices.getProductsById(productId);
            obj.setProductEntity(pro);
            obj.setType(type);
            }
            //TODO price has to check with qty
            cartProductRepository.save(obj);
            return this.getCartByCartId(cartId);
        }catch(Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }

    }

    @Override
    public void update(CartProductDTO cartProductDTO)  {
        CartProductEntity obj =
                cartProductRepository.getCartByProductIdAnduserId(cartProductDTO.getCartId()
                ,cartProductDTO.getProductId(),cartProductDTO.getType());
        obj.setQuantity(cartProductDTO.getQuantity());
        cartProductRepository.save(obj);

    }

    @Override
    public void delete(long productId, long cartId) {
        cartProductRepository.deleteProduct(productId,cartId);
    }

    @Override
    public void deleteListOfCartProduct(Collection<Long> productEntity_id, Long cartEntity_id, Collection<String> type) {
        cartProductRepository.removeAllByProductEntityIdInAndCartEntityIdAndTypeIn(productEntity_id, cartEntity_id,  type);
    }

//    @Override
//    public List<HashMap<Object, Object>> getAllCart(long cartId) {
//        return cartProductRepository.getCartDetail(cartId);
//    }

      @Override
        public List<CartDetailDTO> getAllCart(long cartId) {
            List<CartProductEntity> cartProductEntities = cartProductRepository.getCart(cartId);
            List<CartDetailDTO> cartDetailDTOS = new ArrayList<>();
            for (CartProductEntity cartProduct : cartProductEntities){
                CartDetailDTO cartDetailDTO = new CartDetailDTO();

                cartDetailDTO.setQuantity(cartProduct.getQuantity());

                ProductEntity product = productRepository.findById(cartProduct.getProductEntity().getId())
                        .orElseThrow(()-> new NotFoundException("not found product"));
                //set name and image
                cartDetailDTO.setName(product.getName());
                cartDetailDTO.setImage(product.getImageProduct());

                //get type
                TypeEntity type= typeRepository.findByType(cartProduct.getType());
                cartDetailDTO.setType(cartProduct.getType());
                cartDetailDTO.setPrice(type.getPrice());

                cartDetailDTO.setTotalPrice(cartDetailDTO.getTotalPrice());
                cartDetailDTOS.add(cartDetailDTO);
            }
            return cartDetailDTOS;
        }
    @Override
    public List<CartProductEntity> getCartByCartId(long cartId) {
        return cartProductRepository.getCartByCartId(cartId);
    }
}
