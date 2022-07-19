package com.example.fsoft_shopee_nhom02.mapper;

import com.example.fsoft_shopee_nhom02.dto.SubCategoryDTO;
import com.example.fsoft_shopee_nhom02.model.SubCategoryEntity;

public class SubCategoryMapper {
    public static SubCategoryDTO toSubCategoryDto (SubCategoryEntity subCategory){
        SubCategoryDTO subCategoryDTO = new SubCategoryDTO();
        subCategoryDTO.setId(subCategory.getId());
        subCategoryDTO.setName(subCategory.getName());
        subCategoryDTO.setImage(subCategory.getImage());
        subCategoryDTO.setCategoryId(subCategory.getCategoryEntity().getId());

        return subCategoryDTO;
    }

    public static SubCategoryEntity toEntity (SubCategoryDTO subCategoryDTO){
        SubCategoryEntity subCategory = new SubCategoryEntity();
        subCategory.setId(subCategoryDTO.getId());
        subCategory.setName(subCategoryDTO.getName());
        subCategory.setImage(subCategoryDTO.getImage());

        return subCategory;
    }
}
