package com.blitzscale.inventory_service.controller;

import com.blitzscale.common.event.OrderPlacedEvent;
import com.blitzscale.inventory_service.dto.CreateFlashSaleRequest;
import com.blitzscale.inventory_service.dto.FlashBuyRequest;
import com.blitzscale.inventory_service.dto.OrderResponse;
import com.blitzscale.inventory_service.entity.FlashSale;
import com.blitzscale.inventory_service.service.FlashBuyService;
import com.blitzscale.inventory_service.service.FlashSaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/flash-sales")
@RequiredArgsConstructor
public class FlashSaleController {
    private final FlashSaleService service;
    private final FlashBuyService flashBuyService;

    @PostMapping
    public ResponseEntity<FlashSale> createFlashSale(@RequestBody CreateFlashSaleRequest request) {
        FlashSale createdSale = service.createFlashSale(request);
        return new ResponseEntity<>(createdSale,HttpStatus.CREATED);
    }

    @PostMapping("/{saleId}/buy")
    public ResponseEntity<OrderResponse> buyItem(@PathVariable Long saleId, @RequestBody FlashBuyRequest request) {
        OrderResponse response = flashBuyService.processFlashBuy(saleId, request);

        if ("ACCEPTED".equals(response.getStatus())) {
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
    }
}
