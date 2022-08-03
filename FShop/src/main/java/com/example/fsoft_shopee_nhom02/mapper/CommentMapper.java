package com.example.fsoft_shopee_nhom02.mapper;

import com.example.fsoft_shopee_nhom02.dto.CommentDTO;
import com.example.fsoft_shopee_nhom02.model.CommentEntity;

import java.sql.Timestamp;

public class CommentMapper {
    public static CommentEntity toCommentEntity(CommentDTO commentDTO, CommentEntity comment) {
        comment.setRating(commentDTO.getRating());
        comment.setComment(commentDTO.getComment());
        comment.setCreatedDate(new Timestamp(System.currentTimeMillis()));

        return comment;
    }

    public static CommentDTO toCommentDTO(CommentEntity comment, CommentDTO commentDTO) {
        commentDTO.setId(comment.getId());
        commentDTO.setRating(comment.getRating());
        commentDTO.setComment(comment.getComment());
        commentDTO.setCreatedOn(comment.getCreatedDate());
        commentDTO.setImage(comment.getImage());

        return commentDTO;
    }
}
