package com.example.resturantapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.resturantapp.models.MenuItem;

public interface MenuItemRepo extends JpaRepository<MenuItem, Long> {

    // Use the actual field name of MenuCategory
    List<MenuItem> findByCategory_categoryid(Long categoryid);

    List<MenuItem> findByIsAvailableTrue();

    @Query("SELECT m FROM MenuItem m WHERE m.category.categoryid = :categoryid AND m.isAvailable = true")
    List<MenuItem> findAvailableByCategoryid(@Param("categoryid") Long categoryid);

    @Query("SELECT EXISTS (SELECT 1 FROM OrderItem oi WHERE oi.menuitem.category.categoryid = :categoryid)")
    boolean existsOrdersByCategoryId(@Param("categoryid") Long categoryid);

    MenuItem findByMenuitemname(String menuitemname);
}
