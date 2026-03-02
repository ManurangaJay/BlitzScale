package com.blitzscale.order_service.repository;

import com.blitzscale.order_service.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderTrackingId(String orderTrackingId);
}
