package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.dto.CategoryDTO;
import com.example.fsoft_shopee_nhom02.dto.SuccessResponseDTO;
import com.example.fsoft_shopee_nhom02.service.CategoryService;
import org.apache.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/admin/category")
    public ResponseEntity<?> createNewCategory(@RequestBody CategoryDTO model){
        return ResponseEntity.ok(categoryService.save(model));
    }

    @GetMapping("/category/get-all")
    public ResponseEntity<?> getAllCategory(){
        return ResponseEntity.ok(categoryService.getAllCategory());
    }

    //get all category active
    @GetMapping("/category/get-all-active")
    public ResponseEntity<?> getAllActiveCategory(){
        return ResponseEntity.ok(categoryService.getAllActiveCategory());
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable long id){
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @GetMapping("/category")
    public ResponseEntity<?> getCategoryByShopId(@RequestParam long shopId){
        return ResponseEntity.ok(categoryService.getCategoryByShopId(shopId));
    }

    //get active category by shopId
    @GetMapping("/category/active")
    public ResponseEntity<?> getActiveCategoryByShopId(@RequestParam long shopId){
        return ResponseEntity.ok(categoryService.getActiveCategoryByShopId(shopId));
    }

    @GetMapping("/category/count")
    public ResponseEntity<?> countByShopId(@RequestParam(required = false, defaultValue = "0") long shopId){
        long countProduct = categoryService.countCategoryByShopId(shopId);
        if(countProduct == 0L){
            return ResponseEntity.ok("Empty!!");
        }
        return ResponseEntity.ok(countProduct);
    }

    @GetMapping("/category/active/count")
    public ResponseEntity<?> countActiveByShopId(@RequestParam(required = false, defaultValue = "0") long shopId){
        long countProduct = categoryService.countActiveCategoryByShopId(shopId);
        if(countProduct == 0L){
            return ResponseEntity.ok("Empty!!");
        }
        return ResponseEntity.ok(countProduct);
    }

    @GetMapping("/category/random")
    public ResponseEntity<?> getRandomCategory(@RequestParam(required = false, defaultValue = "") Integer categoryNumber){
        return ResponseEntity.ok(categoryService.getRandomCategory(categoryNumber));
    }

    @PutMapping("/admin/category/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable long id, @RequestBody CategoryDTO model){
        model.setId(id);
        return ResponseEntity.ok(categoryService.update(model));
    }

    @DeleteMapping("/admin/category/{id}")
    public SuccessResponseDTO deleteCategory(@PathVariable long id) throws ParseException {
        categoryService.delete(id);
        return new SuccessResponseDTO(HttpStatus.OK,"Delete success");
    }
    //delete by just set status = Inactive
    @DeleteMapping("/admin/category/hide/{id}")
    public SuccessResponseDTO hideCategory(@PathVariable long id) throws ParseException {
        categoryService.hide(id);
        return new SuccessResponseDTO(HttpStatus.OK,"Delete success");
    }
}
