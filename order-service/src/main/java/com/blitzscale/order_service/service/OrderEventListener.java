package com.blitzscale.order_service.service;

import com.blitzscale.common.event.OrderPlacedEvent;
import com.blitzscale.order_service.dto.PaymentRequest;
import com.blitzscale.order_service.dto.PaymentResponse;
import com.blitzscale.order_service.entity.Order;
import com.blitzscale.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {
    private final OrderRepository orderRepository;
    private final RestClient restClient;

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

        log.info("Successfully processed and saved order: {} to the database.", order.getOrderTrackingId());

        try {
            log.info("Calling Payment Gateway for Order: {} ", order.getOrderTrackingId());
            PaymentResponse paymentResponse = restClient.post()
                    .uri("http://payment-service/api/v1/payments/process")
                    .body(new PaymentRequest(order.getOrderTrackingId(), total, order.getUserId()))
                    .retrieve()
                    .body(PaymentResponse.class);

            if (paymentResponse != null  && "SUCCESS".equals(paymentResponse.getStatus())) {
                order.setStatus("PAID");
                log.info("Order {} fully PAID and confirmed.", order.getOrderTrackingId());
            } else {
                order.setStatus("PAYMENT_FAILED");
                log.error("Order {} failed due to declined payment", order.getOrderTrackingId());
            }
        } catch (Exception e) {
            order.setStatus("PAYMENT_ERROR");
            log.error("Failed to reach Payment Service for Order {}: {}", order.getOrderTrackingId(), e.getMessage());
        }
        orderRepository.save(order);
    }
}
