
package com.example.fsoft_shopee_nhom02.repository;

import com.example.fsoft_shopee_nhom02.model.ProductEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    @Query(value = "SELECT pro FROM ProductEntity pro WHERE pro.name LIKE %?1%")
    List<ProductEntity> findAllBySearchQuery(String query, Pageable pageable);

    @Query(value = "SELECT pro FROM ProductEntity pro WHERE pro.name LIKE %?1%")
    List<ProductEntity> findAllBySearchQuery(String query);

    @Query(value = "SELECT count(pro) FROM ProductEntity pro WHERE pro.name LIKE %?1%")
    long countAllBySearchQuery(String query);

}
