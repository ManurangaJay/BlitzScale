package com.blitzscale.inventory_service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateFlashSaleRequest {
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean active;
    private List<ItemRequest> items;

    @Data
    public static class ItemRequest {
        private Long productId;
        private int stockCount;
        private BigDecimal salePrice;
    }
}
