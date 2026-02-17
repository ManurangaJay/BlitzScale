package com.blitzscale.inventory_service.service;

import com.blitzscale.inventory_service.dto.CreateFlashSaleRequest;
import com.blitzscale.inventory_service.entity.FlashSale;

public interface FlashSaleService {
    public FlashSale createFlashSale(CreateFlashSaleRequest request);
}
