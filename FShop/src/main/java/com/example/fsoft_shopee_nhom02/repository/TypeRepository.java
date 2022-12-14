package com.example.fsoft_shopee_nhom02.repository;

import com.example.fsoft_shopee_nhom02.model.TypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TypeRepository extends JpaRepository<TypeEntity,Long> {
    List<TypeEntity> findAllByProductEntityId(long productId);

    void deleteAllByProductEntityId(long productId);

    @Query(value = "SELECT type.price " +
            "FROM TypeEntity type " +
            "WHERE type.productEntity.id = ?1 GROUP BY type.productEntity.id")
    Long findFirstPrice(long productId);

    @Query(value = "SELECT max(price) FROM types", nativeQuery = true)
    Long findMaxPrice();

    @Query(value = "SELECT min(price) FROM types WHERE product_id = :productId", nativeQuery = true)
    Long findMinPrice(long productId);

    @Query("select type from TypeEntity type where type.type = :type")
    TypeEntity findByType(String type);

    @Query(value = "select o.id,\n" +
            "       o.price,\n" +
            "       o.quantity,\n" +
            "       o.type,\n" +
            "       o.product_id \n" +
            "            from types o \n" +
            "                     left join products pd on o.product_id = pd.id\n" +
            "            where o.product_id=:ProductId and o.type =:TypeProduct", nativeQuery = true)
    TypeEntity findProductByType(@Param("ProductId") Long ProductId, @Param("TypeProduct") String TypeProduct);

    @Query("select type from TypeEntity type where type.productEntity.id =:productId" +
            " and type.type = :type ")
    Optional<TypeEntity> findTypeEntityByProduct(@Param("productId")Long productId, @Param("type")String type);
}
