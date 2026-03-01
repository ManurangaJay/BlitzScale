package com.blitzscale.inventory_service.service.impl;

import com.blitzscale.common.event.OrderPlacedEvent;
import com.blitzscale.inventory_service.dto.FlashBuyRequest;
import com.blitzscale.inventory_service.dto.OrderResponse;
import com.blitzscale.inventory_service.entity.FlashSaleItem;
import com.blitzscale.inventory_service.repository.FlashSaleItemRepository;
import com.blitzscale.inventory_service.repository.ProductRepository;
import com.blitzscale.inventory_service.service.EventPublisher;
import com.blitzscale.inventory_service.service.FlashBuyService;
import com.blitzscale.inventory_service.service.StockCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlashBuyServiceImpl implements FlashBuyService {
    private final ProductRepository productRepository;

    private final StockCacheService stockCacheService;
    private final EventPublisher eventPublisher;
    private final FlashSaleItemRepository flashSaleItemRepository;

    @Override
    public OrderResponse processFlashBuy(Long saleId, FlashBuyRequest request) {

        boolean stockReserved = stockCacheService.deductStockAtomically(
                saleId,
                request.getProductId(),
                request.getQuantity()
        );

        if(!stockReserved) {
            return OrderResponse.builder()
                    .status("FAILED")
                    .message("Item is sold out or unavailable.")
                    .build();
        }

        FlashSaleItem item = flashSaleItemRepository.findByFlashSaleIdAndProductId(saleId, request.getProductId())
                .orElseThrow(() -> new RuntimeException("Sale Item not found"));

        String trackingId = UUID.randomUUID().toString();

        OrderPlacedEvent event = OrderPlacedEvent.builder()
                .orderTrachingId(trackingId)
                .userId(request.getUserId())
                .saleId(saleId)
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .purchasePrice(item.getSalePrice())
                .timestamp(LocalDateTime.now())
                .build();

        eventPublisher.publishOrderEvent(event);

        return OrderResponse.builder()
                .status("ACCEPTED")
                .orderTrackingId(trackingId)
                .message("Order accepted and is being processed.")
                .build();
    }
}
