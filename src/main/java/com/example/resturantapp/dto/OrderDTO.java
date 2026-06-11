package com.example.resturantapp.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.resturantapp.models.Order.OrderStatus;
import com.example.resturantapp.models.Order.OrderType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    private Long orderid;

    private Long customerid;

    @NotBlank(message = "Customer name is required")
    private String customername;

    @NotBlank(message = "Customer phone is required")
    private String phonenumber;

    @NotNull(message = "Order type is required")
    private OrderType ordertype;

    private String tablenumber;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private Double totalprice;

    @Valid
    @Size(min = 1, message = "Order must have at least one item")
    private List<OrderItemDTO> items;

}
