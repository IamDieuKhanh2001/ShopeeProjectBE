package com.example.fsoft_shopee_nhom02.service.impl;

import com.example.fsoft_shopee_nhom02.dto.CategoryDTO;
import com.example.fsoft_shopee_nhom02.exception.BadRequest;
import com.example.fsoft_shopee_nhom02.exception.NotFoundException;
import com.example.fsoft_shopee_nhom02.mapper.CategoryMapper;
import com.example.fsoft_shopee_nhom02.model.CategoryEntity;
import com.example.fsoft_shopee_nhom02.model.ShopEntity;
import com.example.fsoft_shopee_nhom02.repository.CategoryRepository;
import com.example.fsoft_shopee_nhom02.repository.ShopRepository;
import com.example.fsoft_shopee_nhom02.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
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
        CategoryEntity category;

        if(categoryDTO.getShopId() == null){
            throw new BadRequest("Please provide the shop id want to add this category.");
            //category = CategoryMapper.toEntity(categoryDTO);
        }
        else {
            ShopEntity shopEntity = shopRepository.findById(categoryDTO.getShopId()).orElseThrow(()
                    -> new BadRequest("Not found shop with id = " + categoryDTO.getShopId()));
            category = CategoryMapper.toEntity(categoryDTO);
            category.setShopEntity(shopEntity);
        }
        category = categoryRepository.save(category);
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public CategoryDTO update(CategoryDTO categoryDTO) {
        if(categoryDTO.getShopId() == null){
            throw new BadRequest("Please provide the shop id want to add this category.");
            //category = CategoryMapper.toEntity(categoryDTO);
        }
        ShopEntity shopEntity = shopRepository.findById(categoryDTO.getShopId()).orElseThrow(()
                -> new BadRequest("Not found shop with id = "+categoryDTO.getShopId()));

        CategoryEntity category = categoryRepository.findById(categoryDTO.getId()).orElseThrow(()
                -> new BadRequest("Not found category with id = "+categoryDTO.getId()));

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
                new NotFoundException("Not found category with id = "+id));
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public List<CategoryDTO> getCategoryByShopId(long shopId) {
        ShopEntity shop = shopRepository.findById(shopId).orElseThrow(()
                -> new BadRequest("Not found shop with id = "+shopId));
        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        List<CategoryEntity> categories = categoryRepository.findAllByShopEntityId(shopId);

        for (CategoryEntity category : categories){
            categoryDTOS.add(CategoryMapper.toCategoryDto(category));
        }
        if(categoryDTOS.isEmpty()){
            throw new NotFoundException("Empty!!");
        }

        return categoryDTOS;
    }

    @Override
    public long countCategoryByShopId(long shopId) {
        if(shopId == 0l){
            return categoryRepository.count();
        }
        shopRepository.findById(shopId).orElseThrow(() ->
                new BadRequest("Not found shop with id = "+shopId));
        return categoryRepository.countByShopEntityId(shopId);
    }

    @Override
    public void delete(long id) {
        CategoryEntity category = categoryRepository.findById(id).orElseThrow(()
                -> new BadRequest("Category not exist"));
        categoryRepository.deleteById(id);
    }

    @Override
    public List<CategoryDTO> getRandomCategory(Integer limit) {
        limit = (limit == null) ? 6 : limit;
        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        List<CategoryEntity> categories = categoryRepository.findRandomByTop(limit);

        for(CategoryEntity category : categories){
            categoryDTOS.add(CategoryMapper.toCategoryDto(category));
        }

        return categoryDTOS;
    }
}
