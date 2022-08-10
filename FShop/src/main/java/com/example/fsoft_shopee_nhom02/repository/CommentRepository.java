package com.example.fsoft_shopee_nhom02.repository;

import com.example.fsoft_shopee_nhom02.model.CommentEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findAllByProductEntityIdAndStatus(long id, Pageable pageable,
                                                          String status);

    List<CommentEntity> findAllByProductEntityIdAndRating(long id, long rating,
                                                          Pageable pageable, String status);

    long countAllByProductEntityIdAndStatus(long id, String status);

    long countAllByProductEntityIdAndRatingAndStatus(long id, long rating, String status);

    @Query("SELECT sum(c.rating) FROM CommentEntity c WHERE c.productEntity.id = ?1")
    Long sumProductReview(long id);

}
