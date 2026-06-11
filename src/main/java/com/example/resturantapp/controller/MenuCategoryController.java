package com.example.resturantapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.resturantapp.dto.ApiResponseDTO;
import com.example.resturantapp.dto.MenuCategoryDTO;
import com.example.resturantapp.models.MenuCategory;
import com.example.resturantapp.service.MenuCategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/menu/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class MenuCategoryController {
    private final MenuCategoryService menuCategoryService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ApiResponseDTO<MenuCategory>> createCategory(@Valid @RequestBody MenuCategory category) {
        MenuCategory createdCategory = menuCategoryService.createCategory(category);
        return ResponseEntity.ok(ApiResponseDTO.success(createdCategory, "Category created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<MenuCategory>>> getAllCategories() {
        List<MenuCategory> categories = menuCategoryService.getAllCategories();
        return ResponseEntity.ok(ApiResponseDTO.success(categories, "Categories retrieved successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin', 'staff')")
    public ResponseEntity<ApiResponseDTO<MenuCategory>> updateCategory(
            @PathVariable("id") Long categoryid,
            @Valid @RequestBody MenuCategory updatedCategory) {
        MenuCategory category = menuCategoryService.updateCategory(categoryid, updatedCategory);
        return ResponseEntity.ok(ApiResponseDTO.success(category, "Category updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin', 'staff')")
    public ResponseEntity<ApiResponseDTO<MenuCategory>> deleteCategory(@PathVariable("id") Long categoryid) {
        menuCategoryService.deleteCategory(categoryid);
        return ResponseEntity.ok(ApiResponseDTO.success(null, "Category deleted successfully"));
    }
}