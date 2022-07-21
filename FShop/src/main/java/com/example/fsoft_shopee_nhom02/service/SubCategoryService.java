package com.example.fsoft_shopee_nhom02.service;

import com.example.fsoft_shopee_nhom02.dto.SubCategoryDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SubCategoryService {
    SubCategoryDTO save (SubCategoryDTO subCategoryDTO);
    List<SubCategoryDTO> getAllSubCategory ();
    SubCategoryDTO getSubCategoryById(long id);
    List<SubCategoryDTO> getSubCategoryByCategoryId(long categoryId);
    List<SubCategoryDTO> getSubCategoryByShopId(long shopId);
    long countSubCategoryByCategoryId(long categoryId);
    SubCategoryDTO update (SubCategoryDTO subCategoryDTO);
    void delete (long id);
}
