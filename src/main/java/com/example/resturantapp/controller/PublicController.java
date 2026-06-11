package com.example.resturantapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.resturantapp.dto.ApiResponseDTO;
import com.example.resturantapp.dto.OrderDTO;
import com.example.resturantapp.models.Admin;
import com.example.resturantapp.service.AdminService;
import com.example.resturantapp.service.OrderService;
import com.example.resturantapp.mapper.OrderMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicController {
    private final AdminService adminService;
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @GetMapping("/contact")
    public ResponseEntity<ApiResponseDTO<Map<String, String>>> getContact() {
        Admin admin = adminService.getPublicContact();
        Map<String, String> contact = new HashMap<>();
        contact.put("restaurantname", admin.getRestaurantname() != null ? admin.getRestaurantname() : "");
        contact.put("logourl", admin.getLogourl() != null ? admin.getLogourl() : "");
        contact.put("address", admin.getAddress() != null ? admin.getAddress() : "");
        contact.put("workinghours", admin.getWorkinghours() != null ? admin.getWorkinghours() : "");
        contact.put("phone", admin.getAdminphonenumber() != null ? admin.getAdminphonenumber() : "");
        contact.put("email", admin.getEmail() != null ? admin.getEmail() : "");
        return ResponseEntity.ok(ApiResponseDTO.success(contact, "Contact retrieved successfully"));
    }

    @GetMapping("/orders/by-phone/{phone}")
    public ResponseEntity<ApiResponseDTO<List<OrderDTO>>> getOrdersByPhone(@PathVariable String phone) {
        List<OrderDTO> orders = orderMapper.toDTOList(orderService.getOrdersByPhone(phone));
        return ResponseEntity.ok(ApiResponseDTO.success(orders, "Orders retrieved successfully"));
    }

}
