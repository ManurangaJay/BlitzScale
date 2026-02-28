package com.blitzscale.inventory_service.service;

public interface StockCacheService {
    boolean deductStockAtomically(Long saleId, Long productId, int quantity);
}
