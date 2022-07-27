package com.example.fsoft_shopee_nhom02.repository;

import com.example.fsoft_shopee_nhom02.model.CommentEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findAllByProductEntityId(long id, Pageable pageable);
    List<CommentEntity> findAllByProductEntityIdAndRating(long id, long rating, Pageable pageable);

    long countAllByProductEntityId(long id);
    long countAllByProductEntityIdAndRating(long id, long rating);
}
