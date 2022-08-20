package com.example.fsoft_shopee_nhom02.service.impl;

import com.example.fsoft_shopee_nhom02.config.GlobalVariable;
import com.example.fsoft_shopee_nhom02.dto.SubCategoryDTO;
import com.example.fsoft_shopee_nhom02.exception.BadRequest;
import com.example.fsoft_shopee_nhom02.exception.NotFoundException;
import com.example.fsoft_shopee_nhom02.mapper.SubCategoryMapper;
import com.example.fsoft_shopee_nhom02.model.CategoryEntity;
import com.example.fsoft_shopee_nhom02.model.SubCategoryEntity;
import com.example.fsoft_shopee_nhom02.repository.CategoryRepository;
import com.example.fsoft_shopee_nhom02.repository.ProductRepository;
import com.example.fsoft_shopee_nhom02.repository.ShopRepository;
import com.example.fsoft_shopee_nhom02.repository.SubCategoryRepository;
import com.example.fsoft_shopee_nhom02.service.CloudinaryService;
import com.example.fsoft_shopee_nhom02.service.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        if(Objects.equals(category.getStatus(), GlobalVariable.INACTIVE_STATUS)){
            throw new BadRequest("This category is inactive!!");
        }

        SubCategoryEntity subCategory;

        if(subCategoryDTO.getId() == null) {
            //create
            //check name subcategory in 1 category is not similar
            if (subCategoryRepository.findOneByCategoryEntityIdAndName(subCategoryDTO.getCategoryId()
                    , subCategoryDTO.getName()) != null) {
                throw new BadRequest("This name have been used!!");
            }
            subCategory = SubCategoryMapper.toEntity(subCategoryDTO);

        }
        else{ //update
            subCategory  = subCategoryRepository.findById(subCategoryDTO.getId())
                    .orElseThrow(() -> new BadRequest("Not found subcategory with id = "+subCategoryDTO.getId()));

//            if(subCategoryRepository.findByNameAndCategoryIdExceptOldName(subCategoryDTO.getId(),
//                    subCategoryDTO.getCategoryId(),subCategoryDTO.getName()) != null){
//                throw new BadRequest("This name have been used!!");
//            }

            subCategory.setName(subCategoryDTO.getName());
            subCategory.setImage(subCategoryDTO.getImage());
            subCategory.setStatus(subCategoryDTO.getStatus());
        }

        subCategory.setCategoryEntity(category);
        subCategory = subCategoryRepository.save(subCategory);

        return SubCategoryMapper.toSubCategoryDto(subCategory);
    }

    @Override
    public List<SubCategoryDTO> saveListSubcategory(List<SubCategoryDTO> subCategoryDTOS) {
        List<SubCategoryDTO> result = new ArrayList<>();
        for(SubCategoryDTO subCategoryDTO : subCategoryDTOS){
            subCategoryDTO = save(subCategoryDTO);
            result.add(subCategoryDTO);
        }
        return result;
    }

    @Override
    public List<SubCategoryDTO> getAllSubCategory(boolean active) {
        List<SubCategoryDTO> subCategoryDTOS = new ArrayList<>();
        List<SubCategoryEntity> subCategories;
        if(active){
            subCategories = subCategoryRepository.findAllByStatus(GlobalVariable.ACTIVE_STATUS);
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
        if(Objects.equals(category.getStatus(), GlobalVariable.INACTIVE_STATUS)){
            throw new BadRequest("This category is inactive!!");
        }

        List<SubCategoryDTO> subCategoryDTOS = new ArrayList<>();
        List<SubCategoryEntity> subCategories;
        if(active){
            subCategories = subCategoryRepository.findAllByCategoryEntityIdAndStatus(categoryId,GlobalVariable.ACTIVE_STATUS);
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
        shopRepository.findById(shopId).orElseThrow(()
                -> new BadRequest("There is no shop with id = " + shopId));

        List<SubCategoryDTO> subCategoryDTOS = new ArrayList<>();
        List<SubCategoryEntity> subCategories;
        if(active){
            subCategories = subCategoryRepository.findAllByShopEntityIdAndStatus(shopId,GlobalVariable.ACTIVE_STATUS);
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
                return subCategoryRepository.countByStatus(GlobalVariable.ACTIVE_STATUS);
            }
            return subCategoryRepository.countByCategoryEntityIdAndStatus(categoryId,GlobalVariable.ACTIVE_STATUS);
        }
        else{
            if(categoryId == 0L) {
                return subCategoryRepository.count();
            }
            return subCategoryRepository.countByCategoryEntityId(categoryId);
        }
    }

//    @Override
//    public SubCategoryDTO update(SubCategoryDTO subCategoryDTO) {
//        if(subCategoryDTO.getCategoryId() == null){
//            throw new BadRequest("Please provide the category id want to add this subcategory.");
//            //subCategory = subCategoryRepository.save(subCategory);
//        }
//
//        SubCategoryEntity subCategory  = subCategoryRepository.findById(subCategoryDTO.getId())
//                .orElseThrow(() -> new BadRequest("Not found subcategory with id = "+subCategoryDTO.getId()));
//
//        CategoryEntity category = categoryRepository.findById(subCategoryDTO.getCategoryId())
//                .orElseThrow(() -> new BadRequest("Not found category with id = "
//                        +subCategoryDTO.getCategoryId()));
//
//        //check category is active
//        if(Objects.equals(category.getStatus(), GlobalVariable.INACTIVE_STATUS)){
//            throw new BadRequest("This category is inactive!!");
//        }
//
//        //check name subcategory in 1 category is not similar
//        if(subCategoryRepository.findByNameAndCategoryIdExceptOldName(subCategoryDTO.getId(),
//                subCategoryDTO.getCategoryId(),subCategoryDTO.getName()) != null){
//            throw new BadRequest("This name have been used!!");
//        }
//
//        subCategory.setName(subCategoryDTO.getName());
//        subCategory.setImage(subCategoryDTO.getImage());
//        subCategory.setStatus(subCategoryDTO.getStatus());
//        subCategory.setCategoryEntity(category);
//        subCategory = subCategoryRepository.save(subCategory);
//
//        return SubCategoryMapper.toSubCategoryDto(subCategory);
//    }

    @Override
    public void delete(long id) {
        subCategoryRepository.findById(id)
                .orElseThrow(() -> new BadRequest("Fail! This subcategory not exist"));

        subCategoryRepository.deleteById(id);
    }

    @Override
    public void hide(long id) {
        SubCategoryEntity subCategory  = subCategoryRepository.findById(id)
                .orElseThrow(() -> new BadRequest("Fail! This subcategory not exist"));

        //set product inactive
//        List<ProductEntity> products = productRepository.findAllBySubCategoryEntityId(id);
//        for(ProductEntity product : products){
//            product.setStatus("Inactive");
//            product = productRepository.save(product);
//        }

        subCategory.setStatus(GlobalVariable.INACTIVE_STATUS);
        subCategoryRepository.save(subCategory);
    }

    @Override
    public SubCategoryDTO uploadImage(long id,MultipartFile image) {
        SubCategoryEntity subCategory = subCategoryRepository.findById(id)
                .orElseThrow(() -> new BadRequest("Not found subcategory with id = "+id));

        String imageUrl = cloudinaryService.uploadFile(image,String.valueOf(id),
                "ShopeeProject"+ "/" + "Subcategory");

        if(!imageUrl.equals("-1")) {
            subCategory.setImage(imageUrl);
        }
        else if(subCategory.getImage().equals("") || subCategory.getImage().equals("-1"))
            subCategory.setImage("");

        subCategoryRepository.save(subCategory);
        return SubCategoryMapper.toSubCategoryDto(subCategory);
    }

}
