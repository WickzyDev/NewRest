package com.example.resturantapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.resturantapp.dto.ApiResponseDTO;
import com.example.resturantapp.dto.MenuItemDTO;
import com.example.resturantapp.models.MenuItem;
import com.example.resturantapp.service.MenuItemService;

import java.util.List;

@RestController
@RequestMapping("/api/menu/items")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // ✅ allow frontend
public class MenuItemController {

    private final MenuItemService menuitemService;

    // ✅ Create menu item
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ApiResponseDTO<MenuItem>> createMenuItem(@Valid @RequestBody MenuItemDTO menuitemDTO) {
        MenuItem menuitem = menuitemService.createMenuItem(menuitemDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success(menuitem, "Menu item created successfully"));
    }

    // ✅ Get all menu items or filter by category
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<MenuItem>>> getMenuItems(
            @RequestParam(required = false) Long categoryid) {

        List<MenuItem> items = (categoryid != null)
                ? menuitemService.getMenuItemsByCategory(categoryid)
                : menuitemService.getAllMenuItems();

        return ResponseEntity.ok(ApiResponseDTO.success(items, "Menu items retrieved successfully"));
    }

    // ✅ Update a menu item
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ApiResponseDTO<MenuItem>> updateMenuItem(
            @PathVariable("id") Long menuitemid,
            @Valid @RequestBody MenuItemDTO menuItemDTO) {

        MenuItem updated = menuitemService.updateMenuItem(menuitemid, menuItemDTO);
        return ResponseEntity.ok(ApiResponseDTO.success(updated, "Menu item updated successfully"));
    }

    // ✅ Delete a menu item (soft delete)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ApiResponseDTO<Void>> deleteMenuItem(@PathVariable("id") Long menuitemid) {
        menuitemService.deleteMenuItem(menuitemid);
        return ResponseEntity.ok(ApiResponseDTO.success(null, "Menu item deleted successfully"));
    }

    // ✅ Upload image for menu item
    @PostMapping("/{id}/image")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ApiResponseDTO<MenuItem>> uploadImage(
            @PathVariable("id") Long menuitemid,
            @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("No file selected for upload"));
        }

        MenuItem menuItem = menuitemService.uploadImage(menuitemid, file);
        return ResponseEntity.ok(ApiResponseDTO.success(menuItem, "Image uploaded successfully"));
    }
}
