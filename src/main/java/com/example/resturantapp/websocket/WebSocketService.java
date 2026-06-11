package com.example.resturantapp.websocket;

import org.springframework.stereotype.Service;

import com.example.resturantapp.dto.WebSocketMessageDTO;
import com.example.resturantapp.models.Order;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Service
@RequiredArgsConstructor
public class WebSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    public void notifyOrderCreated(Order order) {
        WebSocketMessageDTO message = new WebSocketMessageDTO();
        message.setType("ORDER_CREATED");
        message.setOrderid(order.getOrderid());
        message.setStatus(order.getStatus());
        message.setData(order);

        messagingTemplate.convertAndSend("/topic/orders", message);
    }

    public void notifyOrderStatusUpdated(Order order) {
        WebSocketMessageDTO message = new WebSocketMessageDTO();
        message.setType("STATUS_UPDATED");
        message.setOrderid(order.getOrderid());
        message.setStatus(order.getStatus());
        message.setData(order);

        messagingTemplate.convertAndSend("/topic/orders", message);
        messagingTemplate.convertAndSend("/topic/orders/" + order.getOrderid(), message);
    }
}
