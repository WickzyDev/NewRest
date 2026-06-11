package com.example.resturantapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAuthRequestDTO {

    private String email;

    private String phonenumber;

    // For registration
    private String name;

}
