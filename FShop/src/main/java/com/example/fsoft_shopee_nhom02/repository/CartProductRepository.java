package com.example.fsoft_shopee_nhom02.repository;

import com.example.fsoft_shopee_nhom02.dto.CartDetailDTO;
import com.example.fsoft_shopee_nhom02.model.CartProductEntity;
import org.aspectj.weaver.ast.Var;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartProductRepository extends JpaRepository<CartProductEntity, Long> {
    @Query("Select addCart  FROM CartProductEntity addCart WHERE addCart.cartEntity.id=:cart_id")
    CartProductEntity getCartByCartId(@Param("cart_id")Long cart_id);
    @Query("Select addCart  FROM CartProductEntity addCart" +
            " WHERE addCart.productEntity.id= :product_id and addCart.cartEntity.id=:cart_id and addCart.type= :type")
    CartProductEntity getCartByProductIdAnduserId(@Param("cart_id")Long cart_id, @Param("product_id")Long product_id
            ,@Param("type")String type);

    @Query("Select addCart  FROM CartProductEntity addCart" +
            " WHERE addCart.productEntity.id= :product_id and addCart.cartEntity.id=:cart_id and addCart.type= :type")
    Optional<CartProductEntity> getCartByProductIdAndCartId(@Param("cart_id")Long cart_id, @Param("product_id")Long product_id
            ,@Param("type")String type);
    @Modifying
    @Transactional
    @Query("DELETE  FROM CartProductEntity addCart" +
            " WHERE addCart.cartEntity.id= :cart_id " +
            "and addCart.productEntity.id = :product_id and addCart.type =:type")
    void deleteProduct(@Param("product_id")Long product_id,
                       @Param("cart_id")Long cart_id,
                       @Param("type")String type);

    void removeAllByProductEntityIdInAndCartEntityIdAndTypeIn(Collection<Long> productEntity_id, Long cartEntity_id, Collection<String> type);

   @Query(" select product.name , type.type  , type.price,cart_product.quantity  ,product.image1 " +
            "from CartProductEntity cart_product, TypeEntity  type , ProductEntity  product" +
            " where product.id =cart_product.id and type.type = cart_product.type and cart_product.cartEntity.id =:cart_id")
   List<HashMap<Object,Object>> getCartDetail(@Param("cart_id")Long cart_id);
    @Query(" select c from CartProductEntity c where c.cartEntity.id =:cart_id")
    List<CartProductEntity> getCart(@Param("cart_id")Long cart_id);
}