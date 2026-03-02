package com.blitzscale.order_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_tracking_id", unique = true, nullable = false)
    private String orderTrackingId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "sale_id", nullable = false)
    private Long saleId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Column(nullable = false)
    private String status;

    @Column(name = "order_time", nullable = false)
    private LocalDateTime orderTime;
}
