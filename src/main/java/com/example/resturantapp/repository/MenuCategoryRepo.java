package com.example.resturantapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.resturantapp.models.MenuCategory;

public interface MenuCategoryRepo extends JpaRepository<MenuCategory, Long> {
    MenuCategory findByCategoryname(String categoryname);
}
