package com.blitzscale.inventory_service.service.impl;

import com.blitzscale.inventory_service.dto.CreateFlashSaleRequest;
import com.blitzscale.inventory_service.entity.FlashSale;
import com.blitzscale.inventory_service.entity.FlashSaleItem;
import com.blitzscale.inventory_service.entity.FlashSaleStatus;
import com.blitzscale.inventory_service.entity.Product;
import com.blitzscale.inventory_service.repository.FlashSaleRepository;
import com.blitzscale.inventory_service.repository.ProductRepository;
import com.blitzscale.inventory_service.service.FlashSaleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlashSaleServiceImpl implements FlashSaleService {
    private final FlashSaleRepository flashSaleRepository;
    private final ProductRepository productRepository;
    private final StringRedisTemplate redisTemplate;

    @Override
    @Transactional
    public FlashSale createFlashSale(CreateFlashSaleRequest request){
        log.info("Creating Flash Sale: {}", request.getName());

        // Map the DTO to the Entity
        FlashSale flashSale = FlashSale.builder()
                .name(request.getName())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .status(FlashSaleStatus.PLANNED)
                .build();

        List<FlashSaleItem> items = new ArrayList<>();
        for (CreateFlashSaleRequest.ItemRequest itemRequest : request.getItems()){
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemRequest.getProductId()));

            FlashSaleItem item = FlashSaleItem.builder()
                    .flashSale(flashSale)
                    .product(product)
                    .stockCount(itemRequest.getStockCount())
                    .salePrice(itemRequest.getSalePrice())
                    .build();

            items.add(item);
        }

        flashSale.setItems(items);

        FlashSale savedSale = flashSaleRepository.save(flashSale);
        log.info("Flash Sale saved to DB with ID : ", savedSale.getId());

        warmUpCache(savedSale);
        return savedSale;
    }

    private void warmUpCache(FlashSale sale) {
        sale.getItems().forEach(item -> {
            String redisKey = String.format("sale:%d:item:%d:stock",
                    sale.getId(),
                    item.getProduct().getId());

            redisTemplate.opsForValue().set(redisKey, String.valueOf(item.getStockCount()));

            log.info("Warmed up cache: {} -> {}", redisKey, item.getStockCount() );
        });
    }
}
