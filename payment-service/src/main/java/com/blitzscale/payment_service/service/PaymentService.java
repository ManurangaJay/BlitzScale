package com.blitzscale.payment_service.service;

import com.blitzscale.payment_service.dto.PaymentRequest;
import com.blitzscale.payment_service.dto.PaymentResponse;

public interface PaymentService {
    PaymentResponse processPayment(PaymentRequest request);
}
