package com.blitzscale.inventory_service.repository;

import com.blitzscale.inventory_service.entity.FlashSaleItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FlashSaleItemRepository extends JpaRepository<FlashSaleItem, Long> {
    Optional<FlashSaleItem> findByFlashSaleIdAndProductId(Long saleId, Long productId);
}
