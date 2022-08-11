package com.example.fsoft_shopee_nhom02.repository;

import com.example.fsoft_shopee_nhom02.model.SubCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface SubCategoryRepository extends JpaRepository<SubCategoryEntity,Long> {
    List<SubCategoryEntity> findAllByCategoryEntityId(long categoryId);
    List<SubCategoryEntity> findAllByCategoryEntityIdAndStatus(long categoryId, String status);
    @Query(value = "SELECT s.* FROM subcategories s, categories c WHERE s.category_id = c.id " +
            "AND c.shop_id = :shopId", nativeQuery = true)
    List<SubCategoryEntity> findAllByShopEntityId(long shopId);
    @Query(value = "SELECT s.* FROM subcategories s, categories c WHERE s.category_id = c.id " +
            "AND c.shop_id = :shopId AND s.status = :status", nativeQuery = true)
    List<SubCategoryEntity> findAllByShopEntityIdAndStatus(long shopId, String status);
    List<SubCategoryEntity> findAllByStatus(String status);
    SubCategoryEntity findOneByCategoryEntityIdAndName(long categoryId, String name);
    @Query(value = "SELECT s FROM SubCategoryEntity s WHERE s.id <> :id" +
            " AND s.categoryEntity.id = :categoryId AND s.name = :name")
    SubCategoryEntity findByNameAndCategoryIdExceptOldName(long id, long categoryId, String name);
    long countByCategoryEntityId(long categoryId);
    long countByStatus(String status);
    long countByCategoryEntityIdAndStatus(long categoryId, String status);
}
