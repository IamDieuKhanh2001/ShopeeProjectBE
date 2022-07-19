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
    @Query(value = "SELECT COUNT(*) FROM categories WHERE shop_id = :shopId",nativeQuery = true)
    long getCountByShopId(long shopId);
    @Query(value = "SELECT * FROM categories WHERE shop_id = :shopId",nativeQuery = true)
    List<CategoryEntity> findByShopId(long shopId);
}
