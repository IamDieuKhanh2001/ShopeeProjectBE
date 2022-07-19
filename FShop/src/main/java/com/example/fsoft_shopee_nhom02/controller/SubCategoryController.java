package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.dto.SubCategoryDTO;
import com.example.fsoft_shopee_nhom02.service.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SubCategoryController {

    @Autowired
    private SubCategoryService subCategoryService;

    @PostMapping("/subcategory/admin")
    public ResponseEntity<?> createNewSubCategory(@RequestBody SubCategoryDTO model){
        return ResponseEntity.ok(subCategoryService.save(model));
    }

    @GetMapping("/subcategory/get-all")
    public ResponseEntity<?> getAllSubCategory(){
        return ResponseEntity.ok(subCategoryService.getAllSubCategory());
    }

    @GetMapping("/subcategory/{id}")
    public ResponseEntity<?> getSubCategoryById(@PathVariable long id){
        return ResponseEntity.ok(subCategoryService.getSubCategoryById(id));
    }

    @PutMapping("/subcategory/admin/{id}")
    public ResponseEntity<?> updateSubCategory(@PathVariable long id, @RequestBody SubCategoryDTO model){
        model.setId(id);
        return ResponseEntity.ok(subCategoryService.update(model));
    }

    @DeleteMapping("/subcategory/admin/{id}")
    public ResponseEntity<?> deleteSubCategory(@PathVariable long id){
        subCategoryService.delete(id);
        return ResponseEntity.ok("Success");
    }
}
