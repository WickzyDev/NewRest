package com.example.resturantapp.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.resturantapp.dto.MenuItemDTO;
import com.example.resturantapp.models.MenuCategory;
import com.example.resturantapp.models.MenuItem;
import com.example.resturantapp.repository.MenuCategoryRepo;
import com.example.resturantapp.repository.MenuItemRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuItemService {
    private final MenuItemRepo menuitemRepository;
    private final MenuCategoryRepo menucategoryRepository;

    // ✅ Save uploaded images inside static/uploads directory
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    public MenuItem createMenuItem(MenuItemDTO menuitemDTO) {
        MenuCategory category = menucategoryRepository.findById(menuitemDTO.getCategoryid())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        MenuItem menuitem = new MenuItem();
        menuitem.setCategory(category);
        menuitem.setMenuitemname(menuitemDTO.getMenuitemname());
        menuitem.setDescription(menuitemDTO.getDescription());
        menuitem.setPricesmall(menuitemDTO.getPricesmall());
        menuitem.setPricemedium(menuitemDTO.getPricemedium());
        menuitem.setPricelarge(menuitemDTO.getPricelarge());
        menuitem.setSpecialofferprice(menuitemDTO.getSpecialofferprice());
        menuitem.setIsAvailable(true);

        return menuitemRepository.save(menuitem);
    }

    public List<MenuItem> getAllMenuItems() {
        return menuitemRepository.findByIsAvailableTrue();
    }

    public List<MenuItem> getMenuItemsByCategory(Long categoryid) {
        return menuitemRepository.findAvailableByCategoryid(categoryid);
    }

    public MenuItem updateMenuItem(Long menuitemid, MenuItemDTO menuitemDTO) {
        MenuItem menuitem = menuitemRepository.findById(menuitemid)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));

        MenuCategory category = menucategoryRepository.findById(menuitemDTO.getCategoryid())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        menuitem.setCategory(category);
        menuitem.setMenuitemname(menuitemDTO.getMenuitemname());
        menuitem.setDescription(menuitemDTO.getDescription());
        menuitem.setPricesmall(menuitemDTO.getPricesmall());
        menuitem.setPricemedium(menuitemDTO.getPricemedium());
        menuitem.setPricelarge(menuitemDTO.getPricelarge());
        menuitem.setSpecialofferprice(menuitemDTO.getSpecialofferprice());

        return menuitemRepository.save(menuitem);
    }

    public void deleteMenuItem(Long id) {
        MenuItem menuitem = menuitemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));
        menuitem.setIsAvailable(false);
        menuitemRepository.save(menuitem);
    }

    public MenuItem uploadImage(Long menuitemid, MultipartFile file) {
        MenuItem menuItem = menuitemRepository.findById(menuitemid)
                .orElseThrow(() -> new RuntimeException("MenuItem not found"));

        try {
            // ✅ Create directory if not exists
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // ✅ Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String extension = (originalFilename != null && originalFilename.contains("."))
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : "";
            String fileName = UUID.randomUUID().toString() + extension;

            // ✅ Save file
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            // ✅ Set accessible image URL (served from static/uploads)
            menuItem.setImageurl("/uploads/" + fileName);

            // ✅ Save in DB
            return menuitemRepository.save(menuItem);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }
}
