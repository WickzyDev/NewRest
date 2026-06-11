package com.example.resturantapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.resturantapp.dto.OrderDTO;
import com.example.resturantapp.dto.OrderItemDTO;
import com.example.resturantapp.models.Customer;
import com.example.resturantapp.models.MenuItem;
import com.example.resturantapp.models.Order;
import com.example.resturantapp.models.Order.OrderStatus;
import com.example.resturantapp.models.OrderItem;
import com.example.resturantapp.models.OrderItem.PortionType;
import com.example.resturantapp.models.Role;
import com.example.resturantapp.repository.CustomerRepo;
import com.example.resturantapp.repository.MenuItemRepo;
import com.example.resturantapp.repository.OrderRepo;
import com.example.resturantapp.websocket.WebSocketService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepo orderRepository;
    private final MenuItemRepo menuitemRepository;
    private final CustomerRepo customerRepository;
    private final WebSocketService webSocketService;

    @Transactional
    public Order createOrder(OrderDTO orderDTO) {
        Order order = new Order();
        // Find or create customer by phone number
        Customer customer = customerRepository.findByPhonenumber(orderDTO.getPhonenumber()).orElse(null);
        if (customer == null) {
            customer = new Customer();
            customer.setName(orderDTO.getCustomername());
            customer.setPhonenumber(orderDTO.getPhonenumber());
            customer.setRole(Role.CUSTOMER);
            customer = customerRepository.save(customer);
        }
        order.setCustomer(customer);

        order.setCustomername(orderDTO.getCustomername());
        order.setPhonenumber(orderDTO.getPhonenumber());
        order.setOrdertype(orderDTO.getOrdertype());
        order.setTablenumber(orderDTO.getTablenumber());
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> orderItems = new ArrayList<>();
        double totalPrice = 0.0;
        for (OrderItemDTO itemDTO : orderDTO.getItems()) {
            MenuItem menuitem = menuitemRepository.findById(itemDTO.getMenuitemid())
                    .orElseThrow(() -> new RuntimeException("Menu item not found: " + itemDTO.getMenuitemid()));

            if (!menuitem.getIsAvailable()) {
                throw new RuntimeException("Menu item not available: " + menuitem.getMenuitemname());
            }

            Double price = getPriceForPortion(menuitem, itemDTO.getPortiontype());
            if (price == null || price == 0) {
                throw new RuntimeException("Price not set for portion " + itemDTO.getPortiontype() + " of item "
                        + menuitem.getMenuitemname());
            }
            double itemTotal = price * itemDTO.getQuantity();
            totalPrice += itemTotal;

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMenuitem(menuitem);
            orderItem.setPortiontype(itemDTO.getPortiontype());
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setPrice(price);

            orderItems.add(orderItem);
        }

        order.setItems(orderItems);
        order.setTotalprice(totalPrice);

        Order savedOrder = orderRepository.save(order);
        // Notify by WebSocket
        // webSocketService.notifyOrderCreated(savedOrder); // Commented out to avoid
        // circular reference

        return savedOrder;
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);

        // Notify by WebSocket
        webSocketService.notifyOrderStatusUpdated(updatedOrder);
        return updatedOrder;
    }

    private Double getPriceForPortion(MenuItem menuItem, PortionType portionType) {
        switch (portionType) {
            case SMALL:
                return menuItem.getPricesmall();
            case MEDIUM:
                return menuItem.getPricemedium();
            case LARGE:
                return menuItem.getPricelarge();
            case SPECIAL_OFFER:
                return menuItem.getSpecialofferprice() != null ? menuItem.getSpecialofferprice()
                        : menuItem.getPricemedium();
            default:
                return menuItem.getPricemedium();
        }
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAllOrderByCreatedAtDesc();
    }

    public List<Order> getCustomerOrders(Long customerid) {
        return orderRepository.findByCustomer_customeridOrderByCreatedAtDesc(customerid);
    }

    public Order getOrderById(Long orderid) {
        return orderRepository.findById(orderid)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public List<Order> getCurrentOrders() {
        List<Order> pending = orderRepository.findByStatusOrderByCreatedAtAsc(OrderStatus.PENDING);
        List<Order> preparing = orderRepository.findByStatusOrderByCreatedAtAsc(OrderStatus.PREPARING);
        List<Order> current = new ArrayList<>();
        current.addAll(pending);
        current.addAll(preparing);
        // Sort by createdAt desc
        current.sort((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()));
        return current;
    }

    @Transactional
    public Order updateOrder(Long orderid, OrderDTO orderDTO) {
        Order order = orderRepository.findById(orderid)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Update customer if phone number changed
        Customer customer = customerRepository.findByPhonenumber(orderDTO.getPhonenumber()).orElse(null);
        if (customer == null) {
            customer = new Customer();
            customer.setName(orderDTO.getCustomername());
            customer.setPhonenumber(orderDTO.getPhonenumber());
            customer.setRole(Role.CUSTOMER);
            customer = customerRepository.save(customer);
        }
        order.setCustomer(customer);

        order.setCustomername(orderDTO.getCustomername());
        order.setPhonenumber(orderDTO.getPhonenumber());
        order.setOrdertype(orderDTO.getOrdertype());
        order.setTablenumber(orderDTO.getTablenumber());

        // Recalculate items and total price
        List<OrderItem> orderItems = new ArrayList<>();
        double totalPrice = 0.0;
        for (OrderItemDTO itemDTO : orderDTO.getItems()) {
            MenuItem menuitem = menuitemRepository.findById(itemDTO.getMenuitemid())
                    .orElseThrow(() -> new RuntimeException("Menu item not found: " + itemDTO.getMenuitemid()));

            if (!menuitem.getIsAvailable()) {
                throw new RuntimeException("Menu item not available: " + menuitem.getMenuitemname());
            }

            Double price = getPriceForPortion(menuitem, itemDTO.getPortiontype());
            if (price == null || price == 0) {
                throw new RuntimeException("Price not set for portion " + itemDTO.getPortiontype() + " of item "
                        + menuitem.getMenuitemname());
            }
            double itemTotal = price * itemDTO.getQuantity();
            totalPrice += itemTotal;

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMenuitem(menuitem);
            orderItem.setPortiontype(itemDTO.getPortiontype());
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setPrice(price);

            orderItems.add(orderItem);
        }

        order.setItems(orderItems);
        order.setTotalprice(totalPrice);

        return orderRepository.save(order);
    }

    @Transactional
    public void deleteOrder(Long orderid) {
        Order order = orderRepository.findById(orderid)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        orderRepository.delete(order);
    }

    public List<Order> getOrdersByPhone(String phone) {
        return orderRepository.findByPhonenumberOrderByCreatedAtDesc(phone);
    }

}
