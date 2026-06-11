package com.example.resturantapp.service;

import java.util.Optional;

import com.example.resturantapp.models.Role;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.resturantapp.dto.AuthRequestDTO;
import com.example.resturantapp.dto.AuthResponseDTO;
import com.example.resturantapp.dto.CustomerAuthRequestDTO;
import com.example.resturantapp.models.Admin;
import com.example.resturantapp.models.Customer;
import com.example.resturantapp.models.Staff;
import com.example.resturantapp.repository.AdminRepo;
import com.example.resturantapp.repository.CustomerRepo;
import com.example.resturantapp.repository.StaffRepo;
import com.example.resturantapp.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AdminRepo adminRepository;
    private final StaffRepo staffRepository;
    private final CustomerRepo customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDTO adminLogin(AuthRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        Admin admin = adminRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        String token = jwtUtil.generateToken(admin.getEmail(), Role.ADMIN.name());

        AuthResponseDTO response = new AuthResponseDTO();
        response.setToken(token);
        response.setEmail(admin.getEmail());
        response.setRole(Role.ADMIN);
        response.setName(admin.getRestaurantname());

        return response;
    }

    public AuthResponseDTO staffLogin(AuthRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        Staff staff = staffRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        String token = jwtUtil.generateToken(staff.getEmail(), Role.STAFF.name());

        AuthResponseDTO response = new AuthResponseDTO();
        response.setToken(token);
        response.setEmail(staff.getEmail());
        response.setRole(Role.STAFF);

        return response;
    }

    public AuthResponseDTO customerLogin(CustomerAuthRequestDTO request) {
        Customer customer = findOrCreateCustomer(request);

        String token = jwtUtil.generateToken(
                customer.getEmail() != null ? customer.getEmail() : customer.getPhonenumber(),
                Role.CUSTOMER.name());

        AuthResponseDTO response = new AuthResponseDTO();
        response.setToken(token);
        response.setEmail(customer.getEmail());
        response.setRole(Role.CUSTOMER);
        response.setName(customer.getName());

        return response;
    }

    public AuthResponseDTO registerAdmin(com.example.resturantapp.dto.AdminRegisterDTO request) {
        if (adminRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Admin with this email already exists");
        }

        Admin admin = new Admin();
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setRestaurantname(request.getRestaurantname());
        admin.setAdminphonenumber(request.getAdminphonenumber());
        admin.setAddress(request.getAddress());
        admin.setRole(com.example.resturantapp.models.Role.ADMIN);

        Admin saved = adminRepository.save(admin);

        String token = jwtUtil.generateToken(saved.getEmail(), com.example.resturantapp.models.Role.ADMIN.name());

        AuthResponseDTO response = new AuthResponseDTO();
        response.setToken(token);
        response.setEmail(saved.getEmail());
        response.setRole(com.example.resturantapp.models.Role.ADMIN);
        response.setName(saved.getRestaurantname());

        return response;
    }

    private Customer findOrCreateCustomer(CustomerAuthRequestDTO request) {
        if (request.getEmail() != null) {
            Optional<Customer> existing = customerRepository.findByEmail(request.getEmail());
            if (existing.isPresent()) {
                return existing.get();
            }
        }

        if (request.getPhonenumber() != null) {
            Optional<Customer> existing = customerRepository.findByPhonenumber(request.getPhonenumber());
            if (existing.isPresent()) {
                return existing.get();
            }
        }

        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhonenumber(request.getPhonenumber());

        return customerRepository.save(customer);
    }

}
