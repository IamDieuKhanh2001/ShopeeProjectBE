package com.example.fsoft_shopee_nhom02.service.impl;

import com.example.fsoft_shopee_nhom02.dto.SubCategoryDTO;
import com.example.fsoft_shopee_nhom02.exception.NotFoundException;
import com.example.fsoft_shopee_nhom02.mapper.SubCategoryMapper;
import com.example.fsoft_shopee_nhom02.model.CategoryEntity;
import com.example.fsoft_shopee_nhom02.model.SubCategoryEntity;
import com.example.fsoft_shopee_nhom02.repository.CategoryRepository;
import com.example.fsoft_shopee_nhom02.repository.SubCategoryRepository;
import com.example.fsoft_shopee_nhom02.service.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SubCategoryServiceImpl implements SubCategoryService {

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public SubCategoryDTO save(SubCategoryDTO subCategoryDTO) {
        CategoryEntity category = categoryRepository.findById(subCategoryDTO.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Not found category with id = "
                        +subCategoryDTO.getCategoryId()));

        SubCategoryEntity subCategory = SubCategoryMapper.toEntity(subCategoryDTO);
        subCategory.setCategoryEntity(category);
        subCategory = subCategoryRepository.save(subCategory);

        return SubCategoryMapper.toSubCategoryDto(subCategory);
    }

    @Override
    public List<SubCategoryDTO> getAllSubCategory() {
        List<SubCategoryDTO> subCategoryDTOS = new ArrayList<>();
        List<SubCategoryEntity> subCategories = subCategoryRepository.findAll();
        for(SubCategoryEntity subCategory : subCategories){
            subCategoryDTOS.add(SubCategoryMapper.toSubCategoryDto(subCategory));
        }
        return subCategoryDTOS;
    }

    @Override
    public SubCategoryDTO getSubCategoryById(long id) {
        SubCategoryEntity subCategory  = subCategoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found subcategory with id = "+id));

        return SubCategoryMapper.toSubCategoryDto(subCategory);
    }

    @Override
    public List<SubCategoryDTO> getSubCategoryByCategoryId(long categoryId) {
        List<SubCategoryDTO> subCategoryDTOS = new ArrayList<>();
        List<SubCategoryEntity> subCategories = subCategoryRepository.findAllByCategoryEntityId(categoryId);
        for(SubCategoryEntity subCategory : subCategories){
            subCategoryDTOS.add(SubCategoryMapper.toSubCategoryDto(subCategory));
        }

        if(subCategoryDTOS.isEmpty()){
            throw new NotFoundException("Empty!!");
        }

        return subCategoryDTOS;
    }

    @Override
    public List<SubCategoryDTO> getSubCategoryByShopId(long shopId) {
        List<SubCategoryDTO> subCategoryDTOS = new ArrayList<>();
        List<SubCategoryEntity> subCategories = subCategoryRepository.findAllByShopEntityId(shopId);
        for(SubCategoryEntity subCategory : subCategories){
            subCategoryDTOS.add(SubCategoryMapper.toSubCategoryDto(subCategory));
        }

        if(subCategoryDTOS.isEmpty()){
            throw new NotFoundException("Empty!!");
        }

        return subCategoryDTOS;
    }

    @Override
    public long countSubCategoryByCategoryId(long categoryId) {
        categoryRepository.findById(categoryId).orElseThrow(() ->
                new NotFoundException("Not found category with id = "+categoryId));
        return subCategoryRepository.countByCategoryEntityId(categoryId);
    }

    @Override
    public SubCategoryDTO update(SubCategoryDTO subCategoryDTO) {
        CategoryEntity category = categoryRepository.findById(subCategoryDTO.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Not found category with id = "
                        +subCategoryDTO.getCategoryId()));

        SubCategoryEntity subCategory  = subCategoryRepository.findById(subCategoryDTO.getId())
                .orElseThrow(() -> new NotFoundException("Not found subcategory with id = "+subCategoryDTO.getId()));

        subCategory.setName(subCategoryDTO.getName());
        subCategory.setImage(subCategoryDTO.getImage());
        subCategory.setCategoryEntity(category);
        subCategory = subCategoryRepository.save(subCategory);

        return SubCategoryMapper.toSubCategoryDto(subCategory);
    }

    @Override
    public void delete(long id) {
        subCategoryRepository.deleteById(id);
    }
}
