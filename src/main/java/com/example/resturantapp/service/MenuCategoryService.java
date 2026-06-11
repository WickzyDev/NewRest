package com.example.resturantapp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.resturantapp.models.MenuCategory;
import com.example.resturantapp.repository.MenuCategoryRepo;
import com.example.resturantapp.repository.MenuItemRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuCategoryService {
    private final MenuCategoryRepo menucategoryRepository;
    private final MenuItemRepo menuitemRepository;

    public MenuCategory createCategory(MenuCategory category) {
        return menucategoryRepository.save(category);
    }

    public MenuCategory existByCategoryname(String categoryname) {
        return menucategoryRepository.findByCategoryname(categoryname);
    }

    public List<MenuCategory> getAllCategories() {
        return menucategoryRepository.findAll();
    }

    public void deleteCategory(Long catogoryid) {
        MenuCategory category = menucategoryRepository.findById(catogoryid)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        // Check if there are orders for this category
        if (menuitemRepository.existsOrdersByCategoryId(catogoryid)) {
            throw new RuntimeException(
                    "Cannot delete category because there are orders for its menu items. Please delete the orders first.");
        }
        // Delete all menu items associated with this category
        menuitemRepository.deleteAll(menuitemRepository.findByCategory_categoryid(catogoryid));
        menucategoryRepository.delete(category);
    }

    public MenuCategory updateCategory(Long catogoryid, MenuCategory updatedCategory) {
        MenuCategory category = menucategoryRepository.findById(catogoryid)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setCategoryname(updatedCategory.getCategoryname());
        return menucategoryRepository.save(category);
    }

}
