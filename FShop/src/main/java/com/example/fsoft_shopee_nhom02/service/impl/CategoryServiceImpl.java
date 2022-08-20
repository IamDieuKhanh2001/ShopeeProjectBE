package com.example.fsoft_shopee_nhom02.service.impl;

import com.example.fsoft_shopee_nhom02.config.GlobalVariable;
import com.example.fsoft_shopee_nhom02.dto.CategoryDTO;
import com.example.fsoft_shopee_nhom02.exception.BadRequest;
import com.example.fsoft_shopee_nhom02.exception.NotFoundException;
import com.example.fsoft_shopee_nhom02.mapper.CategoryMapper;
import com.example.fsoft_shopee_nhom02.model.CategoryEntity;
import com.example.fsoft_shopee_nhom02.model.ShopEntity;
import com.example.fsoft_shopee_nhom02.model.SubCategoryEntity;
import com.example.fsoft_shopee_nhom02.repository.CategoryRepository;
import com.example.fsoft_shopee_nhom02.repository.ShopRepository;
import com.example.fsoft_shopee_nhom02.repository.SubCategoryRepository;
import com.example.fsoft_shopee_nhom02.service.CategoryService;
import com.example.fsoft_shopee_nhom02.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SubCategoryRepository subCategoryRepository;
    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public CategoryDTO save(CategoryDTO categoryDTO) {
        CategoryEntity category;

        if(categoryDTO.getShopId() == null){
            throw new BadRequest("Please provide the shop id want to add this category.");
            //category = CategoryMapper.toEntity(categoryDTO);
        }

        ShopEntity shopEntity = shopRepository.findById(categoryDTO.getShopId()).orElseThrow(()
                -> new BadRequest("Not found shop with id = " + categoryDTO.getShopId()));
        //update
        if(categoryDTO.getId() !=null){
            category = categoryRepository.findById(categoryDTO.getId()).orElseThrow(()
                    -> new BadRequest("Not found category with id = "+categoryDTO.getId()));

            if(categoryRepository.findByNameAndShopIdExceptOldName(categoryDTO.getId(),
                    categoryDTO.getShopId(),categoryDTO.getName()) != null){
                throw new BadRequest("This name have been used!!");
            }

            category.setName(categoryDTO.getName());
            category.setImage(categoryDTO.getImage());
            category.setStatus(categoryDTO.getStatus());
        }
        else { //create
            //check name category in 1 shop is not similar
            if (categoryRepository.findOneByShopEntityIdAndName(categoryDTO.getShopId(),
                    categoryDTO.getName()) != null) {
                throw new BadRequest("This name have been used!!");
            }

            category = CategoryMapper.toEntity(categoryDTO);
        }
        category.setShopEntity(shopEntity);

        category = categoryRepository.save(category);
        return CategoryMapper.toCategoryDto(category);
    }

//    @Override
//    public CategoryDTO update(CategoryDTO categoryDTO) {
//        if(categoryDTO.getShopId() == null){
//            throw new BadRequest("Please provide the shop id want to add this category.");
//            //category = CategoryMapper.toEntity(categoryDTO);
//        }
//        ShopEntity shopEntity = shopRepository.findById(categoryDTO.getShopId()).orElseThrow(()
//                -> new BadRequest("Not found shop with id = "+categoryDTO.getShopId()));
//
//        CategoryEntity category = categoryRepository.findById(categoryDTO.getId()).orElseThrow(()
//                -> new BadRequest("Not found category with id = "+categoryDTO.getId()));
//
//        if(categoryRepository.findByNameAndShopIdExceptOldName(categoryDTO.getId(),
//                categoryDTO.getShopId(),categoryDTO.getName()) != null){
//            throw new BadRequest("This name have been used!!");
//        }
//
//        category.setName(categoryDTO.getName());
//        category.setImage(categoryDTO.getImage());
//        category.setStatus(categoryDTO.getStatus());
//        category.setShopEntity(shopEntity);
//        category = categoryRepository.save(category);
//
//        return CategoryMapper.toCategoryDto(category);
//    }

    @Override
    public List<CategoryDTO> getAllCategory(String sort, boolean active) {
        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        List<CategoryEntity> categories;
        if(active) {
            if(sort.equals("desc")){
                categories = categoryRepository.findAllByStatusOrderByIdDesc(GlobalVariable.ACTIVE_STATUS);
            }
            else {
                categories = categoryRepository.findAllByStatus(GlobalVariable.ACTIVE_STATUS);
            }
        }
        else{
            if(sort.equals("desc")){
                categories = categoryRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
            }
            else{
                categories = categoryRepository.findAll();
            }
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
        shopRepository.findById(shopId).orElseThrow(()
                -> new BadRequest("Not found shop with id = "+shopId));
        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        List<CategoryEntity> categories;
        if(active) {
            categories = categoryRepository.findAllByShopEntityIdAndStatus(shopId, GlobalVariable.ACTIVE_STATUS);
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
        shopRepository.findById(shopId).orElseThrow(() ->
                new BadRequest("Not found shop with id = "+shopId));
        if(active) {
            if (shopId == 0L) {
                return categoryRepository.countByStatus(GlobalVariable.ACTIVE_STATUS);
            }
            return categoryRepository.countByShopEntityIdAndStatus(shopId,GlobalVariable.ACTIVE_STATUS);
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
        categoryRepository.findById(id).orElseThrow(()
                -> new BadRequest("Fail!This category not exist"));
        categoryRepository.deleteById(id);
    }

    @Override
    public void hide(long id) {
        CategoryEntity category = categoryRepository.findById(id).orElseThrow(()
                -> new BadRequest("Fail!This category not exist"));

        //set subcategory inactive

        List<SubCategoryEntity> subCategories = subCategoryRepository.findAllByCategoryEntityId(id);
        for(SubCategoryEntity subCategory : subCategories){
            subCategory.setStatus(GlobalVariable.INACTIVE_STATUS);
            subCategoryRepository.save(subCategory);
        }

        category.setStatus(GlobalVariable.INACTIVE_STATUS);

        categoryRepository.save(category);
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

    @Override
    public CategoryDTO uploadImage(long id, MultipartFile image) {
        CategoryEntity category = categoryRepository.findById(id).orElseThrow(()
                -> new BadRequest("This category not exist"));

        String imageUrl = cloudinaryService.uploadFile(image,String.valueOf(id),
                "ShopeeProject"+ "/" + "Category");

        if(imageUrl.equals("-1")){
            category.setImage("");
        }
        else {
            category.setImage(imageUrl);
        }

        categoryRepository.save(category);
        return CategoryMapper.toCategoryDto(category);
    }

}
