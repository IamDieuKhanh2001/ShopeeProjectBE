package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.dto.CategoryDTO;
import com.example.fsoft_shopee_nhom02.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/category/admin")
    public ResponseEntity<?> createNewCategory(@RequestBody CategoryDTO model){
        return ResponseEntity.ok(categoryService.save(model));
    }

    @GetMapping("/category/get-all")
    public ResponseEntity<?> getAllCategory(){
        return ResponseEntity.ok(categoryService.getAllCategory());
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable long id){
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @GetMapping("/category")
    public ResponseEntity<?> getCategoryByShopId(@RequestParam long shopId){
        return ResponseEntity.ok(categoryService.getCategoryByShopId(shopId));
    }

    @GetMapping("/category/count")
    public ResponseEntity<?> countByShopId(@RequestParam long shopId){
        if(categoryService.countCategoryByShopId(shopId) == 0L){
            return ResponseEntity.ok("Empty!!");
        }
        return ResponseEntity.ok(categoryService.countCategoryByShopId(shopId));
    }

    @PutMapping("/category/admin/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable long id, @RequestBody CategoryDTO model){
        model.setId(id);
        return ResponseEntity.ok(categoryService.update(model));
    }

    @DeleteMapping("/category/admin/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable long id){
        categoryService.delete(id);
        return ResponseEntity.ok("Success");
    }
}
