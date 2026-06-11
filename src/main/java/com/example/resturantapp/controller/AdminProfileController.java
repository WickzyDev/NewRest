package com.example.resturantapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.resturantapp.dto.ApiResponseDTO;
import com.example.resturantapp.models.Admin;
import com.example.resturantapp.service.AdminService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/profile")
@RequiredArgsConstructor
public class AdminProfileController {
    private final AdminService adminService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<Admin>> getProfile() {
        Admin admin = adminService.getProfile();
        return ResponseEntity.ok(ApiResponseDTO.success(admin, "Profile retrieved successfully"));
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<Admin>> updateProfile(@Valid @RequestBody Admin adminDetails) {
        Admin admin = adminService.updateProfile(adminDetails);
        return ResponseEntity.ok(ApiResponseDTO.success(admin, "Profile updated successfully"));
    }

    @PostMapping("/logo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<Admin>> uploadLogo(@RequestParam("file") MultipartFile file) {
        Admin admin = adminService.uploadLogo(file);
        return ResponseEntity.ok(ApiResponseDTO.success(admin, "Logo uploaded successfully"));
    }

    @GetMapping("/contact")
    public ResponseEntity<ApiResponseDTO<Map<String, String>>> getContact() {
        Admin admin = adminService.getPublicContact();
        Map<String, String> contact = new HashMap<>();
        contact.put("address", admin.getAddress());
        contact.put("workinghours", admin.getWorkinghours());
        contact.put("phone", admin.getAdminphonenumber());
        contact.put("email", admin.getEmail());
        return ResponseEntity.ok(ApiResponseDTO.success(contact, "Contact retrieved successfully"));
    }
}
