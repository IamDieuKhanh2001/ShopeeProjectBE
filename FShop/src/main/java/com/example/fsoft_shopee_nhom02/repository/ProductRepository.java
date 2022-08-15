
package com.example.fsoft_shopee_nhom02.repository;

import com.example.fsoft_shopee_nhom02.dto.ProductDTO;
import com.example.fsoft_shopee_nhom02.model.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findAllByIdIn(Collection<Long> id);

    long countAllByStatus(String status);

    @Query(value = "SELECT pro FROM ProductEntity pro JOIN " +
            "pro.subCategoryEntity sub JOIN " +
            "sub.categoryEntity cat " +
            "WHERE pro.status = 'Active'")
    Page<ProductEntity> findAllWithCatIdAndSubCatIdAndStatus(Pageable pageable);

    @Query(value = "SELECT pro FROM ProductEntity pro JOIN " +
            "pro.subCategoryEntity sub JOIN " +
            "sub.categoryEntity cat " +
            "WHERE (?1 is null or pro.name LIKE %?1%) " +
            "AND (?2 is null or pro.subCategoryEntity.id = ?2) " +
            "AND (?3 is null or cat.id = ?3) " +
            "AND (?4 is null or pro.fromPrice > ?4 - 1) " +
            "AND (?5 is null or pro.fromPrice < ?5 + 1) " +
            "AND (?6 is null or pro.avgRating >= ?6) " +
            "AND pro.status = 'Active'")
    Page<ProductEntity> findAllBySearchWithCatIdAndSubCatIdAndStatus(String search, Long subId, Long catId, Long minPrice,
                                                                     Long maxPrice, Integer rating, Pageable pageable);
}

