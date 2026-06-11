package com.example.resturantapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.resturantapp.models.Staff;

public interface StaffRepo extends JpaRepository<Staff, Long> {
    Optional<Staff> findByEmail(String email);

    boolean existsByEmail(String email);
}
