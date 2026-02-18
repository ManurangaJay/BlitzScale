package com.blitzscale.inventory_service.controller;

import com.blitzscale.inventory_service.entity.Product;
import com.blitzscale.inventory_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductRepository repository;

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        System.out.println("DEBUG basePrice = " + product.getBasePrice());
        Product savedProduct = repository.save(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }
}
