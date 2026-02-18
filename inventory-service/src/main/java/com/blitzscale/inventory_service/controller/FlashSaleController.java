package com.blitzscale.inventory_service.controller;

import com.blitzscale.inventory_service.dto.CreateFlashSaleRequest;
import com.blitzscale.inventory_service.entity.FlashSale;
import com.blitzscale.inventory_service.service.FlashSaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/flash-sales")
@RequiredArgsConstructor
public class FlashSaleController {
    private final FlashSaleService service;

    @PostMapping
    public ResponseEntity<FlashSale> createFlashSale(@RequestBody CreateFlashSaleRequest request) {
        FlashSale createdSale = service.createFlashSale(request);
        return new ResponseEntity<>(createdSale,HttpStatus.CREATED);
    }
}
