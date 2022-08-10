package com.example.fsoft_shopee_nhom02.service.impl;

import com.example.fsoft_shopee_nhom02.dto.SubCategoryDTO;
import com.example.fsoft_shopee_nhom02.exception.BadRequest;
import com.example.fsoft_shopee_nhom02.exception.NotFoundException;
import com.example.fsoft_shopee_nhom02.mapper.SubCategoryMapper;
import com.example.fsoft_shopee_nhom02.model.CategoryEntity;
import com.example.fsoft_shopee_nhom02.model.ProductEntity;
import com.example.fsoft_shopee_nhom02.model.ShopEntity;
import com.example.fsoft_shopee_nhom02.model.SubCategoryEntity;
import com.example.fsoft_shopee_nhom02.repository.*;
import com.example.fsoft_shopee_nhom02.service.CategoryService;
import com.example.fsoft_shopee_nhom02.service.CloudinaryService;
import com.example.fsoft_shopee_nhom02.service.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

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
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public SubCategoryDTO save(SubCategoryDTO subCategoryDTO) {
        if(subCategoryDTO.getCategoryId() == null){
            throw new BadRequest("Please provide the category id want to add this subcategory.");
            //subCategory = subCategoryRepository.save(subCategory);
        }
        CategoryEntity category = categoryRepository.findById(subCategoryDTO.getCategoryId())
                .orElseThrow(() -> new BadRequest("Not found category with id = "
                        +subCategoryDTO.getCategoryId()));

        //check category is inactive
        if(category.getStatus().equals("Inactive")){
            throw new BadRequest("This category is inactive!!");
        }

        //check name subcategory in 1 category is not similar
        if(checkNameInCategory(subCategoryDTO)){
            throw new BadRequest("This name have been used!!");
        }

        SubCategoryEntity subCategory = SubCategoryMapper.toEntity(subCategoryDTO);
        subCategory.setCategoryEntity(category);
        subCategory = subCategoryRepository.save(subCategory);

        return SubCategoryMapper.toSubCategoryDto(subCategory);
    }

    @Override
    public List<SubCategoryDTO> getAllSubCategory(boolean active) {
        List<SubCategoryDTO> subCategoryDTOS = new ArrayList<>();
        List<SubCategoryEntity> subCategories;
        if(active){
            subCategories = subCategoryRepository.findAllByStatus("Active");
        }
        else {
            subCategories = subCategoryRepository.findAll();
        }
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
    public List<SubCategoryDTO> getSubCategoryByCategoryId(long categoryId, boolean active) {
        CategoryEntity category = categoryRepository.findById(categoryId).orElseThrow(()->
                new BadRequest("There is no category with id = "+categoryId));

        //check category is inactive
        if(category.getStatus().equals("Inactive")){
            throw new BadRequest("This category is inactive!!");
        }

        List<SubCategoryDTO> subCategoryDTOS = new ArrayList<>();
        List<SubCategoryEntity> subCategories;
        if(active){
            subCategories = subCategoryRepository.findAllByCategoryEntityIdAndStatus(categoryId,"Active");
        }
        else {
            subCategories = subCategoryRepository.findAllByCategoryEntityId(categoryId);
        }
        for(SubCategoryEntity subCategory : subCategories){
            subCategoryDTOS.add(SubCategoryMapper.toSubCategoryDto(subCategory));
        }

        if(subCategoryDTOS.isEmpty()){
            throw new NotFoundException("There is no subcategory in this category!");
        }

        return subCategoryDTOS;
    }

    @Override
    public List<SubCategoryDTO> getSubCategoryByShopId(long shopId, boolean active) {
        ShopEntity shop = shopRepository.findById(shopId).orElseThrow(()
                -> new BadRequest("There is no shop with id = "+shopId));

        List<SubCategoryDTO> subCategoryDTOS = new ArrayList<>();
        List<SubCategoryEntity> subCategories;
        if(active){
            subCategories = subCategoryRepository.findAllByShopEntityIdAndStatus(shopId,"Active");
        }
        else {
            subCategories = subCategoryRepository.findAllByShopEntityId(shopId);
        }

        for(SubCategoryEntity subCategory : subCategories){
            subCategoryDTOS.add(SubCategoryMapper.toSubCategoryDto(subCategory));
        }

        if(subCategoryDTOS.isEmpty()){
            throw new NotFoundException("Empty!!");
        }

        return subCategoryDTOS;
    }

    @Override
    public long countSubCategoryByCategoryId(long categoryId, boolean active) {
        categoryRepository.findById(categoryId).orElseThrow(() ->
                new BadRequest("Not found category with id = "+categoryId));

        if(active){
            if(categoryId == 0L) {
                return subCategoryRepository.countByStatus("Active");
            }
            return subCategoryRepository.countByCategoryEntityIdAndStatus(categoryId,"Active");
        }
        else{
            if(categoryId == 0L) {
                return subCategoryRepository.count();
            }
            return subCategoryRepository.countByCategoryEntityId(categoryId);
        }
    }

    @Override
    public SubCategoryDTO update(SubCategoryDTO subCategoryDTO) {
        if(subCategoryDTO.getCategoryId() == null){
            throw new BadRequest("Please provide the category id want to add this subcategory.");
            //subCategory = subCategoryRepository.save(subCategory);
        }

        SubCategoryEntity subCategory  = subCategoryRepository.findById(subCategoryDTO.getId())
                .orElseThrow(() -> new BadRequest("Not found subcategory with id = "+subCategoryDTO.getId()));

        CategoryEntity category = categoryRepository.findById(subCategoryDTO.getCategoryId())
                .orElseThrow(() -> new BadRequest("Not found category with id = "
                        +subCategoryDTO.getCategoryId()));

        //check category is active
        if(category.getStatus().equals("Inactive")){
            throw new BadRequest("This category is inactive!!");
        }

        //check name subcategory in 1 category is not similar
        if(checkNameInCategory(subCategoryDTO)){
            throw new BadRequest("This name have been used!!");
        }

        subCategory.setName(subCategoryDTO.getName());
        subCategory.setImage(subCategoryDTO.getImage());
        subCategory.setStatus(subCategoryDTO.getStatus());
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

    @Override
    public void hide(long id) {
        SubCategoryEntity subCategory  = subCategoryRepository.findById(id)
                .orElseThrow(() -> new BadRequest("Fail! This subcategory not exist"));

        //set product inactive
        List<ProductEntity> products = productRepository.findAllBySubCategoryEntityId(id);
        for(ProductEntity product : products){
            product.setStatus("Inactive");
            product = productRepository.save(product);
        }

        subCategory.setStatus("Inactive");
        subCategory = subCategoryRepository.save(subCategory);
    }

    @Override
    public SubCategoryDTO uploadImage(long id,MultipartFile image) {
        SubCategoryEntity subCategory = subCategoryRepository.findById(id)
                .orElseThrow(() -> new BadRequest("Not found subcategory with id = "+id));

        String imageUrl = cloudinaryService.uploadFile(image,String.valueOf(id),
                "ShopeeProject"+ "/" + "Subcategory");

        if(imageUrl == "-1"){
            subCategory.setImage("");
        }
        else {
            subCategory.setImage(imageUrl);
        }
        subCategoryRepository.save(subCategory);
        return SubCategoryMapper.toSubCategoryDto(subCategory);
    }

    //check name subcategory can not similar in category
    public boolean checkNameInCategory(SubCategoryDTO subCategoryDTO){
        boolean check = false;
        List<SubCategoryEntity> subCategories = subCategoryRepository
                .findAllByCategoryEntityId(subCategoryDTO.getCategoryId());

        for(SubCategoryEntity subCategory : subCategories){
            if(subCategory.getName().equals(subCategoryDTO.getName())){
                check = true;
                break;
            }
        }
        return check;
    }
}
