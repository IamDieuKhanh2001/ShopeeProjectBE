package com.example.fsoft_shopee_nhom02.repository;

import com.example.fsoft_shopee_nhom02.model.TypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeRepository extends JpaRepository<TypeEntity,Long> {
    List<TypeEntity> findAllByProductEntityId(long productId);

    void deleteAllByProductEntityId(long productId);

    @Query(value = "SELECT type.price FROM TypeEntity type WHERE type.productEntity.id = ?1")
    List<Long> findFirstPrice(long productId);

    @Query(value = "SELECT max(price) FROM types", nativeQuery = true)
    long findMaxPrice();
}
