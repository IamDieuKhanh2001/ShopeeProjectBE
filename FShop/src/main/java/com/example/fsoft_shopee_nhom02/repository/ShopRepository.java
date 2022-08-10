package com.example.fsoft_shopee_nhom02.repository;

import com.example.fsoft_shopee_nhom02.model.ShopEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface ShopRepository extends JpaRepository<ShopEntity,Long> {
    List<ShopEntity> findByNameContaining(String keyword);
    ShopEntity findOneByName(String name);
    @Query(value = "SELECT * FROM shops WHERE name = :name AND id <> :id",nativeQuery = true)
    ShopEntity findByNameExceptCurrentName(long id, String name);
    @Query(value = "SELECT sum(t.quantity) FROM CategoryEntity c, SubCategoryEntity s, ProductEntity p, TypeEntity t" +
            " WHERE p.id = t.productEntity.id AND s.id = p.subCategoryEntity.id AND c.id = s.categoryEntity.id " +
            "AND c.shopEntity.id = :shopId AND p.status = 'Active' AND c.status = 'Active' AND s.status = 'Active'")
    long countTotalProduct(long shopId);
}
