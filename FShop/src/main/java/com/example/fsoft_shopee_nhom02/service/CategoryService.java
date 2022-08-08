package com.example.fsoft_shopee_nhom02.service;

import com.example.fsoft_shopee_nhom02.dto.CategoryDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    CategoryDTO save(CategoryDTO categoryDTO);
    CategoryDTO update(CategoryDTO categoryDTO);
    List<CategoryDTO> getAllCategory(boolean active);
    CategoryDTO getCategoryById(long id);
    List<CategoryDTO> getCategoryByShopId(long shopId, boolean active);
    long countCategoryByShopId(long shopId, boolean active);
    void delete(long id);
    void hide(long id);
    List<CategoryDTO> getRandomCategory(Integer limit);

}
