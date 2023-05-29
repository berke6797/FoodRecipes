package com.bilgeadam.controller;

import com.bilgeadam.dto.request.CreateCategoryRequestDto;
import com.bilgeadam.dto.request.UpdateCategoryRequestDto;
import com.bilgeadam.repository.entity.Category;
import com.bilgeadam.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static com.bilgeadam.constant.ApiUrls.*;
import java.util.List;

@RestController
@RequestMapping(CATEGORY)
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    @PostMapping(CREATE_CATEGORY+"/{token}")
    public ResponseEntity<Boolean> createCategory(@PathVariable String token,@RequestBody CreateCategoryRequestDto dto){
        return ResponseEntity.ok(categoryService.createCategory(token,dto));
    }
    @PutMapping(UPDATE_CATEGORY+"/{token}")
    public ResponseEntity<Boolean> updateCategory(@PathVariable String token, @RequestBody UpdateCategoryRequestDto dto){
        return ResponseEntity.ok(categoryService.updateCategory(token,dto));
    }
    @DeleteMapping(DELETE_CATEGORY+"/{token}/{categoryId}")
    public ResponseEntity<Boolean> deleteCategory(@PathVariable String token,@PathVariable String categoryId){
        return ResponseEntity.ok(categoryService.deleteCategory(token,categoryId));
    }
    @GetMapping(FIND_ALL_CATEGORY)
    public ResponseEntity<List<Category>> findAllCategory(){
        return ResponseEntity.ok(categoryService.findAll());
    }
}
