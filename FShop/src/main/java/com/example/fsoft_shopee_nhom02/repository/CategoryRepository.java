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
    CategoryEntity findOneByShopEntityIdAndName(long shopId, String name);
    @Query(value = "SELECT c FROM CategoryEntity c WHERE c.id <> :id " +
            "AND c.shopEntity.id = :shopId AND c.name = :name")
    CategoryEntity findByNameAndShopIdExceptOldName(long id, long shopId, String name);
    List<CategoryEntity> findAllByShopEntityId(long shopId);
    List<CategoryEntity> findAllByStatus(String status);
    List<CategoryEntity> findAllByStatusOrderByIdDesc(String status);
    List<CategoryEntity> findAllByShopEntityIdAndStatus(long shopId,String status);
    @Query(value = "SELECT * FROM categories WHERE status = 'Active' ORDER BY rand() limit ?",nativeQuery = true)
    List<CategoryEntity> findRandomByTop(Integer limit);
}
