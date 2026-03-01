package com.blitzscale.inventory_service.service;

import com.blitzscale.common.event.OrderPlacedEvent;

public interface EventPublisher {
    void publishOrderEvent(OrderPlacedEvent event);
}
