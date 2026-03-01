package com.blitzscale.inventory_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponse {
    private String status;
    private String orderTrackingId;
    private String message;
}
