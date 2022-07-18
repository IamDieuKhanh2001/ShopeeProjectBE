package com.example.fsoft_shopee_nhom02.service.impl;

import com.example.fsoft_shopee_nhom02.dto.CategoryDTO;
import com.example.fsoft_shopee_nhom02.exception.NotFoundException;
import com.example.fsoft_shopee_nhom02.mapper.CategoryMapper;
import com.example.fsoft_shopee_nhom02.model.CategoryEntity;
import com.example.fsoft_shopee_nhom02.model.ShopEntity;
import com.example.fsoft_shopee_nhom02.repository.CategoryRepository;
import com.example.fsoft_shopee_nhom02.repository.ShopRepository;
import com.example.fsoft_shopee_nhom02.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public CategoryDTO save(CategoryDTO categoryDTO) {
        ShopEntity shopEntity = shopRepository.findById(categoryDTO.getShopId()).orElseThrow(()
                -> new NotFoundException("Shop not exist"));
        CategoryEntity category = CategoryMapper.toEntity(categoryDTO);
        category.setShopEntity(shopEntity);
        category = categoryRepository.save(category);

        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public CategoryDTO update(CategoryDTO categoryDTO) {
        ShopEntity shopEntity = shopRepository.findById(categoryDTO.getShopId()).orElseThrow(()
                -> new NotFoundException("Shop not exist"));

        CategoryEntity category = categoryRepository.findById(categoryDTO.getId()).orElseThrow(()
                -> new NotFoundException("Category not exist"));

        category.setName(categoryDTO.getName());
        category.setImage(categoryDTO.getImage());
        category.setShopEntity(shopEntity);
        category = categoryRepository.save(category);

        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public List<CategoryDTO> getAllCategory() {

        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        List<CategoryEntity> categories = categoryRepository.findAll();
        for (CategoryEntity category : categories){
            categoryDTOS.add(CategoryMapper.toCategoryDto(category));
        }
        return categoryDTOS;
    }

    @Override
    public CategoryDTO getCategoryById(long id) {
        CategoryEntity category = categoryRepository.findById(id).orElseThrow(()->
                new NotFoundException("Category not exist"));
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public void delete(long id) {
        categoryRepository.deleteById(id);
    }
}
