package com.example.resturantapp.dto;

import com.example.resturantapp.models.OrderItem.PortionType;

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
public class OrderItemDTO {

    private Long orderitemid; // added

    private Long orderid; // added

    @NotNull(message = "Menu item ID is required")
    private Long menuitemid;

    @NotNull(message = "Portion type is required")
    private PortionType portiontype;

    @Positive(message = "Quantity must be positive")
    private Integer quantity;

    private Double price;

    private String menuitemname;

}
