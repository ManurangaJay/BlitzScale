package com.blitzscale.payment_service.service.impl;

import com.blitzscale.payment_service.dto.PaymentRequest;
import com.blitzscale.payment_service.dto.PaymentResponse;
import com.blitzscale.payment_service.entity.Payment;
import com.blitzscale.payment_service.repository.PaymentRepository;
import com.blitzscale.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;

    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        log.info("Processing payment for Order: {} Amount : ${}", request.getOrderId(), request.getAmount());

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String transactionId = UUID.randomUUID().toString();
        String status = (Math.random() > 0.1) ? "SUCCESS" : "DECLINED";

        Payment payment = Payment.builder()
                .transactionId(transactionId)
                .orderTrackingId(request.getOrderId())
                .userId(request.getUserId())
                .amount(request.getAmount())
                .status(status)
                .processedAt(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);

        log.info("Payment {} saved to database with status: {}", transactionId, status);

        return new PaymentResponse(transactionId, status);
    }
}
