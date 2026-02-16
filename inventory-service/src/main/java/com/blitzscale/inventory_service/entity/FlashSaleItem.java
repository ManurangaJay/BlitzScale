package com.blitzscale.inventory_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "flash_sale_items", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"sale_id", "product_id"})
})
@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class FlashSaleItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id", nullable = false)
    private FlashSale flashSale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "stock_count", nullable = false)
    private int stockCount;

    @Column(name = "sale_price", nullable = false)
    private BigDecimal salePrice;
}
