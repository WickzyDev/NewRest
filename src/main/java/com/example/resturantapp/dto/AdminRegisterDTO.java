package com.example.resturantapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdminRegisterDTO {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private String restaurantname;

    private String adminphonenumber;

    private String address;
}
