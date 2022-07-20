package com.example.fsoft_shopee_nhom02.repository;

import com.example.fsoft_shopee_nhom02.model.SubCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface SubCategoryRepository extends JpaRepository<SubCategoryEntity,Long> {
    List<SubCategoryEntity> findAllByCategoryEntityId(long categoryId);
    long countByCategoryEntityId(long categoryId);
}
