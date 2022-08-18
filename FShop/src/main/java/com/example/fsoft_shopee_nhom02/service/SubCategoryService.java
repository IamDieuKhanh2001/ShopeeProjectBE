package com.example.fsoft_shopee_nhom02.service;

import com.example.fsoft_shopee_nhom02.dto.SubCategoryDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface SubCategoryService {
    SubCategoryDTO save (SubCategoryDTO subCategoryDTO);
    List<SubCategoryDTO> createListSubcategory (List<SubCategoryDTO> subCategoryDTOS);
    List<SubCategoryDTO> getAllSubCategory(boolean active);
    SubCategoryDTO getSubCategoryById(long id);
    List<SubCategoryDTO> getSubCategoryByCategoryId(long categoryId, boolean active);
    List<SubCategoryDTO> getSubCategoryByShopId(long shopId, boolean active);
    long countSubCategoryByCategoryId(long categoryId, boolean active);
    //SubCategoryDTO update (SubCategoryDTO subCategoryDTO);
    void delete (long id);
    void hide (long id);
    SubCategoryDTO uploadImage(long id,MultipartFile image);
}
