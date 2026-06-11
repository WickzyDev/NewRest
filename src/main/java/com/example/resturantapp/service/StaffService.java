package com.example.resturantapp.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.resturantapp.dto.AuthRequestDTO;
import com.example.resturantapp.dto.StaffDTO;
import com.example.resturantapp.models.Role;
import com.example.resturantapp.models.Staff;
import com.example.resturantapp.repository.StaffRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StaffService {
    private final StaffRepo staffRepository;
    private final PasswordEncoder passwordEncoder;

    public Staff createStaff(StaffDTO request) {
        if (staffRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Staff with this email already exists");
        }

        Staff staff = new Staff();
        staff.setStaffname(request.getName());
        staff.setEmail(request.getEmail());
        staff.setPhonenumber(request.getPhonenumber());
        staff.setPassword(passwordEncoder.encode(request.getPassword()));
        staff.setRole(Role.STAFF);

        return staffRepository.save(staff);
    }

    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    public Staff updateStaff(Long staffid, StaffDTO request) {
        Staff staff = staffRepository.findById(staffid)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        staff.setStaffname(request.getName());
        staff.setEmail(request.getEmail());
        staff.setPhonenumber(request.getPhonenumber());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            staff.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return staffRepository.save(staff);
    }

    public void deleteStaff(Long staffid) {
        Staff staff = staffRepository.findById(staffid)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        staffRepository.delete(staff);
    }

}