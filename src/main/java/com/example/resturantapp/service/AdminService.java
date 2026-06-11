package com.example.resturantapp.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.resturantapp.models.Admin;
import com.example.resturantapp.repository.AdminRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepo adminRepository;

    private final String UPLOAD_DIR = "src/main/resources/static/uploads/logo/";

    public Admin getProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return adminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
    }

    public Admin updateProfile(Admin adminDetails) {
        Admin admin = getProfile();
        admin.setRestaurantname(adminDetails.getRestaurantname());
        admin.setAddress(adminDetails.getAddress());
        admin.setAdminphonenumber(adminDetails.getAdminphonenumber());
        admin.setWorkinghours(adminDetails.getWorkinghours());
        return adminRepository.save(admin);
    }

    public Admin uploadLogo(MultipartFile file) {
        Admin admin = getProfile();

        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            admin.setLogourl("/uploads/logo/" + fileName);
            return adminRepository.save(admin);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload logo", e);
        }
    }

    public Admin getPublicContact() {
        // Assuming there's only one admin, get the first one
        return adminRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("No admin found"));
    }
}
