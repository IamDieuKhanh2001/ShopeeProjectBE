package com.example.fsoft_shopee_nhom02.mapper;

import com.example.fsoft_shopee_nhom02.dto.CategoryDTO;
import com.example.fsoft_shopee_nhom02.model.CategoryEntity;

public class CategoryMapper {
    public static CategoryDTO toCategoryDto (CategoryEntity category){
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setImage(category.getImage());
        categoryDTO.setShopId(category.getShopEntity().getId());

        return categoryDTO;
    }

    public static CategoryEntity toEntity (CategoryDTO categoryDTO){
        CategoryEntity category = new CategoryEntity();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());
        category.setImage(categoryDTO.getImage());

        return category;
    }
}
