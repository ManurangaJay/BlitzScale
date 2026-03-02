package com.blitzscale.order_service.service;

import com.blitzscale.common.event.OrderPlacedEvent;
import com.blitzscale.order_service.entity.Order;
import com.blitzscale.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {
    private final OrderRepository orderRepository;

    @KafkaListener(topics = "order-placed-topic", groupId = "order-processing-group")
    public void handleOrderPlacedEvent(OrderPlacedEvent event) {
        log.info("Received Order Event for Tracking ID: {}", event.getOrderTrachingId());

        BigDecimal total = event.getPurchasePrice().multiply(BigDecimal.valueOf(event.getQuantity()));

        Order order = Order.builder()
                .orderTrackingId(event.getOrderTrachingId())
                .userId(event.getUserId())
                .saleId(event.getSaleId())
                .productId(event.getProductId())
                .quantity(event.getQuantity())
                .totalPrice(total)
                .status("CONFIRMED")
                .orderTime(event.getTimestamp())
                .build();

        orderRepository.save(order);
        log.info("Successfully processed and saved order: {} to the database.", order.getOrderTrackingId());
    }
}
