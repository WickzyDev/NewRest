package com.example.resturantapp.dto;

import com.example.resturantapp.models.Order.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketMessageDTO {
    // to ORDER_CREATED, STATUS_UPDATED, ORDER_COMPLETED
    private String type;
    private Long orderid;
    private OrderStatus status;
    private Object data;
}
