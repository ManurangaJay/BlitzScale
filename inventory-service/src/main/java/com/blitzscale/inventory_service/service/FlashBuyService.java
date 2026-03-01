package com.blitzscale.inventory_service.service;

import com.blitzscale.inventory_service.dto.FlashBuyRequest;
import com.blitzscale.inventory_service.dto.OrderResponse;

public interface FlashBuyService {
    public OrderResponse processFlashBuy(Long saleId, FlashBuyRequest request);
}
