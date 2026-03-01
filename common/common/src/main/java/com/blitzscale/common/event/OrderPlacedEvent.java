package com.blitzscale.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPlacedEvent {
    private String orderTrachingId;
    private String userId;
    private Long saleId;
    private Long productId;
    private int quantity;
    private BigDecimal purchasePrice;
    private LocalDateTime timestamp;
}