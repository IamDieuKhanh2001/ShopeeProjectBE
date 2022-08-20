package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.dto.SubCategoryDTO;
import com.example.fsoft_shopee_nhom02.dto.SuccessResponseDTO;
import com.example.fsoft_shopee_nhom02.service.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;

@RestController
public class SubCategoryController {

    @Autowired
    private SubCategoryService subCategoryService;

    @PostMapping("/admin/subcategory")
    public ResponseEntity<?> createNewSubCategory(@RequestBody List<SubCategoryDTO> models){
        return ResponseEntity.ok(subCategoryService.saveListSubcategory(models));
    }

    @GetMapping("/subcategory/get-all")
    public ResponseEntity<?> getAllSubCategory(){
        return ResponseEntity.ok(subCategoryService.getAllSubCategory(false));
    }

    //get all category active
    @GetMapping("/subcategory/get-all-active")
    public ResponseEntity<?> getAllActiveSubCategory(){
        return ResponseEntity.ok(subCategoryService.getAllSubCategory(true));
    }

    @GetMapping("/subcategory/{id}")
    public ResponseEntity<?> getSubCategoryById(@PathVariable long id){
        return ResponseEntity.ok(subCategoryService.getSubCategoryById(id));
    }

    @GetMapping("/subcategory/get")
    public ResponseEntity<?> getSubCategoryByCategoryId(@RequestParam long categoryId){
        return ResponseEntity.ok(subCategoryService.getSubCategoryByCategoryId(categoryId,false));
    }

    //get list active subcategory in category
    @GetMapping("/subcategory/active/get")
    public ResponseEntity<?> getActiveSubCategoryByCategoryId(@RequestParam long categoryId){
        return ResponseEntity.ok(subCategoryService.getSubCategoryByCategoryId(categoryId,true));
    }

    @GetMapping("/subcategory")
    public ResponseEntity<?> getSubCategoryByShopId(@RequestParam long shopId) {
        return ResponseEntity.ok(subCategoryService.getSubCategoryByShopId(shopId,false));
    }

    //get list active subcategory in shop
    @GetMapping("/subcategory/active")
    public ResponseEntity<?> getActiveSubCategoryByShopId(@RequestParam long shopId){
        return ResponseEntity.ok(subCategoryService.getSubCategoryByShopId(shopId,true));
    }

    @GetMapping("/subcategory/count")
    public ResponseEntity<?> countSubCategoryByCategoryId(@RequestParam(required = false,
            defaultValue = "0") long categoryId){
        long count = subCategoryService.countSubCategoryByCategoryId(categoryId,false);
        if(count == 0L){
            return ResponseEntity.ok("Empty!!");
        }
        return ResponseEntity.ok(count);

    }

    //count active subcategory in category
    @GetMapping("/subcategory/active/count")
    public ResponseEntity<?> countActiveSubCategoryByCategoryId(@RequestParam(required = false,
            defaultValue = "0") long categoryId){
        long count = subCategoryService.countSubCategoryByCategoryId(categoryId,true);
        if(count == 0L){
            return ResponseEntity.ok("Empty!!");
        }
        return ResponseEntity.ok(count);
    }

    @PutMapping("/admin/subcategory/{id}")
    public ResponseEntity<?> updateSubCategory(@PathVariable long id, @RequestBody SubCategoryDTO model){
        model.setId(id);
        return ResponseEntity.ok(subCategoryService.save(model));
    }

    @PutMapping("/admin/subcategory/updateAll")
    public ResponseEntity<?> updateSubCategories(@RequestBody List<SubCategoryDTO> model){
        return ResponseEntity.ok(subCategoryService.saveListSubcategory(model));
    }

    @DeleteMapping("/admin/subcategory/{id}")
    public SuccessResponseDTO deleteSubCategory(@PathVariable long id){
        subCategoryService.delete(id);
        return new SuccessResponseDTO(HttpStatus.OK,"Delete success");
    }

    //delete by just set status = Inactive
    @DeleteMapping("/admin/subcategory/hide/{id}")
    public SuccessResponseDTO hideSubCategory(@PathVariable long id){
        subCategoryService.hide(id);
        return new SuccessResponseDTO(HttpStatus.OK,"Delete success");
    }

}
