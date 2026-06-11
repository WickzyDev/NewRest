package com.example.resturantapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemDTO {

    private Long menuitemid;

    @NotNull(message = "Category ID is required")
    private Long categoryid;

    @NotBlank(message = "Name is required")
    private String menuitemname;

    private String description;
    private String imageurl;

    @Positive(message = "Price must be positive")
    private Double pricesmall;

    @Positive(message = "Price must be positive")
    private Double pricemedium;

    @Positive(message = "Price must be positive")
    private Double pricelarge;

    private Double specialofferprice;

    private Boolean isAvailable = true;
}
