package com.example.resturantapp.service;

import java.util.Collections;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.example.resturantapp.models.Admin;
import com.example.resturantapp.models.Staff;
import com.example.resturantapp.repository.AdminRepo;
import com.example.resturantapp.repository.StaffRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepo adminRepository;
    private final StaffRepo staffRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // if user is Admin
        Admin admin = adminRepository.findByEmail(username).orElse(null);
        if (admin != null) {
            return new User(admin.getEmail(), admin.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        }

        // if user is Staff
        Staff staff = staffRepository.findByEmail(username).orElse(null);
        if (staff != null) {
            return new User(staff.getEmail(), staff.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_STAFF")));
        }

        // If not found, throw error
        throw new UsernameNotFoundException("User not found with email: " + username);
    }
}
