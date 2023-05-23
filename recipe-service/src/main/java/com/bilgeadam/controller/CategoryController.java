package com.bilgeadam.controller;

import com.bilgeadam.dto.request.CreateCategoryRequestDto;
import com.bilgeadam.dto.request.UpdateCategoryRequestDto;
import com.bilgeadam.repository.entity.Category;
import com.bilgeadam.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    @PostMapping("/create-category/{token}")
    public ResponseEntity<Boolean> createCategory(@PathVariable String token,@RequestBody CreateCategoryRequestDto dto){
        return ResponseEntity.ok(categoryService.createCategory(token,dto));
    }
    @PutMapping("/update-category/{token}")
    public ResponseEntity<Boolean> updateCategory(@PathVariable String token, @RequestBody UpdateCategoryRequestDto dto){
        return ResponseEntity.ok(categoryService.updateCategory(token,dto));
    }
    @DeleteMapping("/create-category/{token}/{categoryId}")
    public ResponseEntity<Boolean> deleteCategory(@PathVariable String token,@PathVariable String categoryId){
        return ResponseEntity.ok(categoryService.deleteCategory(token,categoryId));
    }
    @GetMapping("/find-all-category")
    public ResponseEntity<List<Category>> findAllRecipe(){
        return ResponseEntity.ok(categoryService.findAll());
    }
}
