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

    @PostMapping("/category")
    public ResponseEntity<?> createNewCategory(@RequestBody CategoryDTO model){
        return ResponseEntity.ok(categoryService.save(model));
    }

    @GetMapping("/category")
    public ResponseEntity<?> getAllCategory(){
        return ResponseEntity.ok(categoryService.getAllCategory());
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable long id){
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PutMapping("/category/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable long id, @RequestBody CategoryDTO model){
        model.setId(id);
        return ResponseEntity.ok(categoryService.update(model));
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id){
        categoryService.delete(id);
        return ResponseEntity.ok("Success");
    }
}
