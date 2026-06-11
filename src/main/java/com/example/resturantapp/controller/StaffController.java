package com.example.resturantapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.resturantapp.dto.ApiResponseDTO;
import com.example.resturantapp.dto.StaffDTO;
import com.example.resturantapp.models.Staff;
import com.example.resturantapp.service.StaffService;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class StaffController {
    private final StaffService staffService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<Staff>> createStaff(@Valid @RequestBody StaffDTO request) {
        Staff staff = staffService.createStaff(request);
        return ResponseEntity.ok(ApiResponseDTO.success(staff, "Staff created successfully"));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ApiResponseDTO<List<Staff>>> getAllStaff() {
        List<Staff> staffList = staffService.getAllStaff();
        return ResponseEntity.ok(ApiResponseDTO.success(staffList, "Staff retrieved successfully"));
    }

    @PutMapping("/{staffid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<Staff>> updateStaff(@PathVariable Long staffid,
            @RequestBody StaffDTO request) {
        Staff staff = staffService.updateStaff(staffid, request);
        return ResponseEntity.ok(ApiResponseDTO.success(staff, "Staff updated successfully"));
    }

    @DeleteMapping("/{staffid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<String>> deleteStaff(@PathVariable Long staffid) {
        staffService.deleteStaff(staffid);
        return ResponseEntity.ok(ApiResponseDTO.success(null, "Staff deleted successfully"));
    }
}
