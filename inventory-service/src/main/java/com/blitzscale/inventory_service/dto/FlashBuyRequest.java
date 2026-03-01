package com.blitzscale.inventory_service.dto;

import lombok.Data;

@Data
public class FlashBuyRequest {
    private Long productId;
    private int quantity;
    private String userId;
}
