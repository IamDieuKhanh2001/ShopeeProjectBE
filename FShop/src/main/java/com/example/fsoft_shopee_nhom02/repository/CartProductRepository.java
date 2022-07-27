package com.example.fsoft_shopee_nhom02.repository;

import com.example.fsoft_shopee_nhom02.model.CartProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartProductRepository extends JpaRepository<CartProductEntity, Long> {
    @Query("Select addCart  FROM CartProductEntity addCart WHERE addCart.cartEntity.id=:cart_id")
    List<CartProductEntity> getCartByuserId(@Param("cart_id")Long cart_id);
    @Query("Select addCart  FROM CartProductEntity addCart WHERE addCart.productEntity.id= :product_id and addCart.cartEntity.id=:cart_id")
    Optional<CartProductEntity> getCartByProductIdAnduserId(@Param("cart_id")Long cart_id, @Param("product_id")Long product_id);
}