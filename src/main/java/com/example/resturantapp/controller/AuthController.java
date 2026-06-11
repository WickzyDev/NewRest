package com.example.resturantapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.resturantapp.dto.ApiResponseDTO;
import com.example.resturantapp.dto.AuthRequestDTO;
import com.example.resturantapp.dto.AuthResponseDTO;
import com.example.resturantapp.dto.CustomerAuthRequestDTO;
import com.example.resturantapp.service.AuthService;
import com.example.resturantapp.dto.AdminRegisterDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/admin/login")
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> adminLogin(@Valid @RequestBody AuthRequestDTO request) {
        AuthResponseDTO response = authService.adminLogin(request);
        return ResponseEntity.ok(ApiResponseDTO.success(response, "Admin login successful"));
    }

    @PostMapping("/staff/login")
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> staffLogin(@Valid @RequestBody AuthRequestDTO request) {
        AuthResponseDTO response = authService.staffLogin(request);
        return ResponseEntity.ok(ApiResponseDTO.success(response, "Staff login successful"));
    }

    @PostMapping("/customer/login")
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> customerLogin(
            @Valid @RequestBody CustomerAuthRequestDTO request) {
        AuthResponseDTO response = authService.customerLogin(request);
        return ResponseEntity.ok(ApiResponseDTO.success(response, "Customer login successful"));
    }

    @PostMapping("/register-admin")
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> registerAdmin(
            @Valid @RequestBody AdminRegisterDTO request) {
        AuthResponseDTO response = authService.registerAdmin(request);
        return ResponseEntity.ok(ApiResponseDTO.success(response, "Admin registered"));
    }
}
