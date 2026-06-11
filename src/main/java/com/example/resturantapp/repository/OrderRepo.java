package com.example.resturantapp.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.resturantapp.models.Order;
import com.example.resturantapp.models.Order.OrderStatus;

public interface OrderRepo extends JpaRepository<Order, Long> {
    List<Order> findByCustomer_customeridOrderByCreatedAtDesc(Long customerid);

    List<Order> findByStatusOrderByCreatedAtAsc(OrderStatus status);

    List<Order> findByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime start, LocalDateTime end);

    @Query("SELECT o FROM Order o ORDER BY o.createdAt DESC")
    List<Order> findAllOrderByCreatedAtDesc();

    List<Order> findByPhonenumberOrderByCreatedAtDesc(String phonenumber);

    long countByStatus(OrderStatus status);
}
