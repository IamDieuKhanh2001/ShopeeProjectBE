package com.example.fsoft_shopee_nhom02.service.impl;

import com.example.fsoft_shopee_nhom02.dto.SubCategoryDTO;
import com.example.fsoft_shopee_nhom02.exception.BadRequest;
import com.example.fsoft_shopee_nhom02.exception.NotFoundException;
import com.example.fsoft_shopee_nhom02.mapper.SubCategoryMapper;
import com.example.fsoft_shopee_nhom02.model.CategoryEntity;
import com.example.fsoft_shopee_nhom02.model.ShopEntity;
import com.example.fsoft_shopee_nhom02.model.SubCategoryEntity;
import com.example.fsoft_shopee_nhom02.repository.CategoryRepository;
import com.example.fsoft_shopee_nhom02.repository.ShopRepository;
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
    @Autowired
    private ShopRepository shopRepository;

    @Override
    public SubCategoryDTO save(SubCategoryDTO subCategoryDTO) {
        if(subCategoryDTO.getCategoryId() == null){
            throw new BadRequest("Please provide the category id want to add this subcategory.");
            //subCategory = subCategoryRepository.save(subCategory);
        }
        CategoryEntity category = categoryRepository.findById(subCategoryDTO.getCategoryId())
                .orElseThrow(() -> new BadRequest("Not found category with id = "
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
        if(subCategoryDTOS.isEmpty()){
            throw new NotFoundException("Empty!!");
        }
        return subCategoryDTOS;
    }

    @Override
    public SubCategoryDTO getSubCategoryById(long id) {
        SubCategoryEntity subCategory  = subCategoryRepository.findById(id)
                .orElseThrow(() -> new BadRequest("Not found subcategory with id = "+id));

        return SubCategoryMapper.toSubCategoryDto(subCategory);
    }

    @Override
    public List<SubCategoryDTO> getSubCategoryByCategoryId(long categoryId) {
        CategoryEntity category = categoryRepository.findById(categoryId).orElseThrow(()->
                new BadRequest("There is no category with id = "+categoryId));

        List<SubCategoryDTO> subCategoryDTOS = new ArrayList<>();
        List<SubCategoryEntity> subCategories = subCategoryRepository.findAllByCategoryEntityId(categoryId);
        for(SubCategoryEntity subCategory : subCategories){
            subCategoryDTOS.add(SubCategoryMapper.toSubCategoryDto(subCategory));
        }

        if(subCategoryDTOS.isEmpty()){
            throw new NotFoundException("There is no subcategory in this category!");
        }

        return subCategoryDTOS;
    }

    @Override
    public List<SubCategoryDTO> getSubCategoryByShopId(long shopId) {
        ShopEntity shop = shopRepository.findById(shopId).orElseThrow(()
                -> new BadRequest("There is no shop with id = "+shopId));

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
        if(categoryId == 0l){
            return subCategoryRepository.count();
        }
        categoryRepository.findById(categoryId).orElseThrow(() ->
                new BadRequest("Not found category with id = "+categoryId));
        return subCategoryRepository.countByCategoryEntityId(categoryId);
    }

    @Override
    public SubCategoryDTO update(SubCategoryDTO subCategoryDTO) {
        if(subCategoryDTO.getCategoryId() == null){
            throw new BadRequest("Please provide the category id want to add this subcategory.");
            //subCategory = subCategoryRepository.save(subCategory);
        }

        CategoryEntity category = categoryRepository.findById(subCategoryDTO.getCategoryId())
                .orElseThrow(() -> new BadRequest("Not found category with id = "
                        +subCategoryDTO.getCategoryId()));

        SubCategoryEntity subCategory  = subCategoryRepository.findById(subCategoryDTO.getId())
                .orElseThrow(() -> new BadRequest("Not found subcategory with id = "+subCategoryDTO.getId()));

        subCategory.setName(subCategoryDTO.getName());
        subCategory.setImage(subCategoryDTO.getImage());
        subCategory.setCategoryEntity(category);
        subCategory = subCategoryRepository.save(subCategory);

        return SubCategoryMapper.toSubCategoryDto(subCategory);
    }

    @Override
    public void delete(long id) {
        SubCategoryEntity subCategory  = subCategoryRepository.findById(id)
                .orElseThrow(() -> new BadRequest("Fail! This subcategory not exist"));

        subCategoryRepository.deleteById(id);
    }
}
