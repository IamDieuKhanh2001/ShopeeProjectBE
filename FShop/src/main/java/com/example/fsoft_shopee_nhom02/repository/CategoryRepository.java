package com.example.fsoft_shopee_nhom02.repository;

import com.example.fsoft_shopee_nhom02.model.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface CategoryRepository extends JpaRepository<CategoryEntity,Long> {
    long countByShopEntityId(long shopId);
    long countByStatus(String status);
    long countByShopEntityIdAndStatus(long shopId,String status);
    List<CategoryEntity> findAllByShopEntityId(long shopId);
    List<CategoryEntity> findAllByStatus(String status);
    List<CategoryEntity> findAllByShopEntityIdAndStatus(long shopId,String status);
    @Query(value = "SELECT * FROM categories WHERE status = 'Active' ORDER BY rand() limit ?",nativeQuery = true)
    List<CategoryEntity> findRandomByTop(Integer limit);
}
