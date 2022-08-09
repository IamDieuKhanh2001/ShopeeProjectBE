package com.example.fsoft_shopee_nhom02.service.impl;

import com.example.fsoft_shopee_nhom02.dto.CategoryDTO;
import com.example.fsoft_shopee_nhom02.exception.BadRequest;
import com.example.fsoft_shopee_nhom02.exception.NotFoundException;
import com.example.fsoft_shopee_nhom02.mapper.CategoryMapper;
import com.example.fsoft_shopee_nhom02.model.CategoryEntity;
import com.example.fsoft_shopee_nhom02.model.ShopEntity;
import com.example.fsoft_shopee_nhom02.repository.CategoryRepository;
import com.example.fsoft_shopee_nhom02.repository.ShopRepository;
import com.example.fsoft_shopee_nhom02.repository.SubCategoryRepository;
import com.example.fsoft_shopee_nhom02.service.CategoryService;
import com.example.fsoft_shopee_nhom02.service.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private SubCategoryService subCategoryService;

    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Override
    public CategoryDTO save(CategoryDTO categoryDTO) {
        CategoryEntity category;

        if(categoryDTO.getShopId() == null){
            throw new BadRequest("Please provide the shop id want to add this category.");
            //category = CategoryMapper.toEntity(categoryDTO);
        }

        ShopEntity shopEntity = shopRepository.findById(categoryDTO.getShopId()).orElseThrow(()
                -> new BadRequest("Not found shop with id = " + categoryDTO.getShopId()));

        //check name category in 1 shop is not similar
        if(checkNameInShop(categoryDTO)){
            throw new BadRequest("This name have been used!!");
        }

        category = CategoryMapper.toEntity(categoryDTO);
        category.setShopEntity(shopEntity);

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

        //check name category in 1 shop is not similar
        if(checkNameInShop(categoryDTO)){
            throw new BadRequest("This name have been used!!");
        }

        category.setName(categoryDTO.getName());
        category.setImage(categoryDTO.getImage());
        category.setStatus(categoryDTO.getStatus());
        category.setShopEntity(shopEntity);
        category = categoryRepository.save(category);

        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public List<CategoryDTO> getAllCategory(boolean active) {
        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        List<CategoryEntity> categories;
        if(active) {
            categories = categoryRepository.findAllByStatus("Active");
        }
        else{
            categories = categoryRepository.findAll();
        }
        for (CategoryEntity category : categories){
            categoryDTOS.add(CategoryMapper.toCategoryDto(category));
        }
        if(categoryDTOS.isEmpty()){
            throw new NotFoundException("Empty!!");
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
    public List<CategoryDTO> getCategoryByShopId(long shopId, boolean active) {
        ShopEntity shop = shopRepository.findById(shopId).orElseThrow(()
                -> new BadRequest("Not found shop with id = "+shopId));
        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        List<CategoryEntity> categories;
        if(active) {
            categories = categoryRepository.findAllByShopEntityIdAndStatus(shopId, "Active");
        }
        else{
            categories = categoryRepository.findAllByShopEntityId(shopId);
        }

        for (CategoryEntity category : categories){
            categoryDTOS.add(CategoryMapper.toCategoryDto(category));
        }
        if(categoryDTOS.isEmpty()){
            throw new NotFoundException("Empty!!");
        }

        return categoryDTOS;
    }

    @Override
    public long countCategoryByShopId(long shopId, boolean active) {
        ShopEntity shop = shopRepository.findById(shopId).orElseThrow(() ->
                new BadRequest("Not found shop with id = "+shopId));
        if(active) {
            if (shopId == 0L) {
                return categoryRepository.countByStatus("Active");
            }
            return categoryRepository.countByShopEntityIdAndStatus(shopId,"Active");
        }
        else{
            if (shopId == 0L) {
                return categoryRepository.count();
            }
            return categoryRepository.countByShopEntityId(shopId);
        }
    }

    @Override
    public void delete(long id) {
        CategoryEntity category = categoryRepository.findById(id).orElseThrow(()
                -> new BadRequest("Fail!This category not exist"));
        categoryRepository.deleteById(id);
    }

    @Override
    public void hide(long id) {
        CategoryEntity category = categoryRepository.findById(id).orElseThrow(()
                -> new BadRequest("Fail!This category not exist"));

        //set subcategory inactive
        //subCategoryService.hide(id);

//        List<SubCategoryEntity> subCategories = subCategoryRepository.findAllByCategoryEntityId(id);
//        for(SubCategoryEntity subCategory : subCategories){
//            subCategory.setStatus("Inactive");
//            subCategory = subCategoryRepository.save(subCategory);
//        }

        category.setStatus("Inactive");

        category = categoryRepository.save(category);
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
    //check name category can not similar in shop
    public boolean checkNameInShop(CategoryDTO categoryDTO){
        boolean check = false;
        List<CategoryEntity> categories = categoryRepository.findAllByShopEntityId(categoryDTO.getShopId());

        for(CategoryEntity category : categories){
            if(category.getName().equals(categoryDTO.getName())){
                check = true;
                break;
            }
        }
        return check;
    }
}
