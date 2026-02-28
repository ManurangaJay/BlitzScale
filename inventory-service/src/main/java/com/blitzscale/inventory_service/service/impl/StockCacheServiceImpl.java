package com.blitzscale.inventory_service.service.impl;

import com.blitzscale.inventory_service.service.StockCacheService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockCacheServiceImpl implements StockCacheService {
    private final StringRedisTemplate redisTemplate;
    private DefaultRedisScript<Long> script;

    @PostConstruct
    public void init() {
        script = new DefaultRedisScript<>();
        script.setScriptSource(new ResourceScriptSource(new ClassPathResource("scripts/stock_decrement.lua")));
    }

    @Override
    public boolean deductStockAtomically(Long saleId, Long productId, int quantity) {
        String redisKey = String.format("sale:%d:item:%d:stock", saleId, productId);

        Long result = redisTemplate.execute(
                script,
                Collections.singletonList(redisKey),
                String.valueOf(quantity)
        );

        if (result == null) {
            log.error("Lua script returned null for key: {}", redisKey);
            return false;
        }

        if (result == 1L) {
            log.info("Successfully reserved {} items for key: {}", quantity, redisKey);
            return true;
        } else if (result == 0L) {
            log.warn("Out of stock for key: {}", redisKey);
            return false;
        } else {
            log.error("Stock key not found in Redis: {}" , redisKey);
            return false;
        }
    }
}
