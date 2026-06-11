package com.example.resturantapp.controller;

import org.springframework.web.bind.annotation.GetMapping;

import com.example.resturantapp.dto.ApiResponseDTO;
import com.example.resturantapp.dto.OrderDTO;
import com.example.resturantapp.mapper.OrderMapper;
import com.example.resturantapp.models.Order;
import com.example.resturantapp.models.Order.OrderStatus;
import com.example.resturantapp.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<OrderDTO>> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        Order order = orderService.createOrder(orderDTO);
        return ResponseEntity.ok(ApiResponseDTO.success(orderMapper.toDTO(order), "Order created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<OrderDTO>>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity
                .ok(ApiResponseDTO.success(orderMapper.toDTOList(orders), "Orders retrieved successfully"));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponseDTO<List<OrderDTO>>> getMyOrders(Authentication authentication) {
        // In a real implementation, you'd get customer ID from authentication
        // For demo, using a fixed customer ID
        List<Order> orders = orderService.getCustomerOrders(1L);
        return ResponseEntity
                .ok(ApiResponseDTO.success(orderMapper.toDTOList(orders), "Orders retrieved successfully"));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponseDTO<OrderDTO>> updateOrderStatus(
            @PathVariable("id") Long orderid, @RequestBody String status) {
        Order order = orderService.updateOrderStatus(orderid, OrderStatus.valueOf(status));
        return ResponseEntity.ok(ApiResponseDTO.success(orderMapper.toDTO(order), "Order status updated successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<OrderDTO>> getOrder(@PathVariable Long orderid) {
        Order order = orderService.getOrderById(orderid);
        return ResponseEntity.ok(ApiResponseDTO.success(orderMapper.toDTO(order), "Order retrieved successfully"));
    }

    @GetMapping("/current")
    public ResponseEntity<ApiResponseDTO<List<OrderDTO>>> getCurrentOrders() {
        List<Order> orders = orderService.getCurrentOrders();
        return ResponseEntity
                .ok(ApiResponseDTO.success(orderMapper.toDTOList(orders), "Current orders retrieved successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<Void>> deleteOrder(@PathVariable("id") Long orderid) {
        orderService.deleteOrder(orderid);
        return ResponseEntity.ok(ApiResponseDTO.success(null, "Order deleted successfully"));
    }
}
