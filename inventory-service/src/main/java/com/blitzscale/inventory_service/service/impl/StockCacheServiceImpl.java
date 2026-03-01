package com.blitzscale.inventory_service.service.impl;

import com.blitzscale.inventory_service.service.StockCacheService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockCacheServiceImpl implements StockCacheService {
    private final StringRedisTemplate redisTemplate;
    private DefaultRedisScript<String> script;

    @PostConstruct
    public void init() {
        script = new DefaultRedisScript<>();
        script.setScriptSource(new ResourceScriptSource(new ClassPathResource("scripts/stock_decrement.lua")));
        script.setResultType(String.class);
    }

    @Override
    public boolean deductStockAtomically(Long saleId, Long productId, int quantity) {
        String redisKey = String.format("sale:%d:item:%d:stock", saleId, productId);

        String result = redisTemplate.execute(
                script,
                Collections.singletonList(redisKey),
                String.valueOf(quantity)
        );

        if (result == null) {
            log.error("Lua script returned null for key: {}", redisKey);
            return false;
        }

        return switch (result) {
            case "1" -> {
                log.info("Successfully reserved {} items for key: {}", quantity, redisKey);
                yield true;
            }
            case "0" -> {
                log.warn("Out of stock for key: {}", redisKey);
                yield false;
            }
            case "-1" -> {
                log.error("Stock key not found in Redis: {}", redisKey);
                yield false;
            }
            default -> {
                log.error("Unexpected result from Lua script: {}", result);
                yield false;
            }
        };
    }
}
