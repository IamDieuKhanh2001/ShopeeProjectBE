
package com.example.fsoft_shopee_nhom02.repository;

import com.example.fsoft_shopee_nhom02.model.ProductEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    @Query(value = "SELECT pro FROM ProductEntity pro " +
            "WHERE pro.name " +
            "LIKE %?1% AND pro.status = 'Active'")
    List<ProductEntity> findAllBySearchQuery(String query, Pageable pageable);

    List<ProductEntity> findAllByStatus(String status, Pageable pageable);

    List<ProductEntity> findAllByStatus(String status);

    @Query(value = "SELECT pro FROM ProductEntity pro WHERE " +
            "pro.name LIKE %?1% " +
            "AND pro.status = 'Active'")
    List<ProductEntity> findAllBySearchQuery(String query);

    @Query(value = "SELECT count(pro) FROM ProductEntity pro WHERE " +
            "pro.name LIKE %?1% " +
            "AND pro.status = 'Active'")
    long countAllBySearchQuery(String query);

    long countAllByStatus(String status);

    @Query(value = "SELECT pro FROM ProductEntity pro " +
            "WHERE pro.name " +
            "LIKE %?1% " +
            "AND pro.subCategoryEntity.id = ?2 " +
            "AND pro.status = 'Active'")
    List<ProductEntity> findAllBySearchAndSubCateAndStatus(String keyword, long id);

    @Query(value = "SELECT pro FROM ProductEntity pro " +
            "WHERE pro.name LIKE %?1% " +
            "AND pro.subCategoryEntity.id = ?2 " +
            "AND pro.status = 'Active'")
    List<ProductEntity> findAllBySearchAndSubCateAndStatus(String keyword, long id,
                                                           Pageable pageable);

    @Query(value = "SELECT count(pro) " +
            "FROM ProductEntity pro " +
            "WHERE pro.name LIKE %?1% " +
            "AND pro.subCategoryEntity.id = ?2 " +
            "AND pro.status = 'Active'")
    long countAllBySearchAndSubCateAndStatus(String keyword, long id);

    List<ProductEntity> findAllBySubCategoryEntityIdAndStatus(long id,
                                                              Pageable pageable, String status);

    List<ProductEntity> findAllBySubCategoryEntityIdAndStatus(long id, String status);

    List<ProductEntity> findAllBySubCategoryEntityId(long id);

    @Query(value = "SELECT count(pro) " +
            "FROM ProductEntity pro " +
            "WHERE pro.subCategoryEntity.id = ?1 AND pro.status = 'Active'")
    long countAllBySubCate(long id);

    @Query(value = "SELECT pro FROM ProductEntity pro JOIN " +
            "pro.subCategoryEntity sub JOIN " +
            "sub.categoryEntity cat " +
            "WHERE cat.id = ?1 " +
            "AND pro.status = 'Active'")
    List<ProductEntity> findAllByCatIdAndStatus(long id, Pageable pageable);

    @Query(value = "SELECT pro FROM ProductEntity pro JOIN " +
            "pro.subCategoryEntity sub JOIN " +
            "sub.categoryEntity cat " +
            "WHERE cat.id = ?1 " +
            "AND pro.status = 'Active'")
    List<ProductEntity> findAllByCatIdAndStatus(long id);

    @Query(value = "SELECT count(pro) FROM ProductEntity pro JOIN " +
            "pro.subCategoryEntity sub JOIN " +
            "sub.categoryEntity cat " +
            "WHERE cat.id = ?1 " +
            "AND pro.status = 'Active'")
    long countAllByCatIdAndStatus(long id);

    @Query(value = "SELECT pro FROM ProductEntity pro JOIN " +
            "pro.subCategoryEntity sub JOIN " +
            "sub.categoryEntity cat " +
            "WHERE cat.id = ?1 " +
            "AND pro.name LIKE %?2%" +
            "AND pro.status = 'Active'")
    List<ProductEntity> findAllBySearchAndByCatIdAndStatus(String search, long id);

    @Query(value = "SELECT pro FROM ProductEntity pro JOIN " +
            "pro.subCategoryEntity sub JOIN " +
            "sub.categoryEntity cat " +
            "WHERE cat.id = ?2 " +
            "AND pro.name LIKE %?1%" +
            "AND pro.status = 'Active'")
    List<ProductEntity> findAllBySearchAndByCatIdAndStatus(String search, long id, Pageable pageable);

    @Query(value = "SELECT count(pro) FROM ProductEntity pro JOIN " +
            "pro.subCategoryEntity sub JOIN " +
            "sub.categoryEntity cat " +
            "WHERE cat.id = ?2 " +
            "AND pro.name LIKE %?1%" +
            "AND pro.status = 'Active'")
    long countAllBySearchAndByCatIdAndStatus(String search, long id);

    @Query(value = "SELECT pro FROM ProductEntity pro JOIN " +
            "pro.subCategoryEntity sub JOIN " +
            "sub.categoryEntity cat " +
            "WHERE cat.id = ?1 " +
            "AND sub.id = ?2 " +
            "AND pro.status = 'Active'")
    List<ProductEntity> findAllByCatIdAndSubCatIdAndStatus(long id, long subId);

    @Query(value = "SELECT count(pro) FROM ProductEntity pro JOIN " +
            "pro.subCategoryEntity sub JOIN " +
            "sub.categoryEntity cat " +
            "WHERE cat.id = ?1 " +
            "AND sub.id = ?2 " +
            "AND pro.status = 'Active'")
    long countAllByCatIdAndSubCatIdAndStatus(long id, long subId);

    @Query(value = "SELECT pro FROM ProductEntity pro JOIN " +
            "pro.subCategoryEntity sub JOIN " +
            "sub.categoryEntity cat " +
            "WHERE cat.id = ?1 " +
            "AND sub.id = ?2 " +
            "AND pro.status = 'Active'")
    List<ProductEntity> findAllByCatIdAndSubCatIdAndStatus(long id, long subId,
                                                           Pageable pageable);

    @Query(value = "SELECT pro FROM ProductEntity pro JOIN " +
            "pro.subCategoryEntity sub JOIN " +
            "sub.categoryEntity cat " +
            "WHERE pro.name LIKE %?1%" +
            "AND cat.id = ?2 " +
            "AND sub.id = ?3 " +
            "AND pro.status = 'Active'")
    List<ProductEntity> findAllBySearchAndCatIdAndSubCatIdAndStatus(String search, long id,
                                                                    long subId, Pageable pageable);

    @Query(value = "SELECT pro FROM ProductEntity pro JOIN " +
            "pro.subCategoryEntity sub JOIN " +
            "sub.categoryEntity cat " +
            "WHERE pro.name LIKE %?1%" +
            "AND cat.id = ?2 " +
            "AND sub.id = ?3 " +
            "AND pro.status = 'Active'")
    List<ProductEntity> findAllBySearchAndCatIdAndSubCatIdAndStatus(String search, long id,
                                                                    long subId);

    @Query(value = "SELECT count(pro) FROM ProductEntity pro JOIN " +
            "pro.subCategoryEntity sub JOIN " +
            "sub.categoryEntity cat " +
            "WHERE pro.name LIKE %?1%" +
            "AND cat.id = ?2 " +
            "AND sub.id = ?3 " +
            "AND pro.status = 'Active'")
    long countAllBySearchAndCatIdAndSubCatIdAndStatus(String search, long id, long subId);
}

