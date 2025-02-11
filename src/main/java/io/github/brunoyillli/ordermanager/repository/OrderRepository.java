package io.github.brunoyillli.ordermanager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.brunoyillli.ordermanager.entity.Order;
import io.github.brunoyillli.ordermanager.enums.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByStatus(OrderStatus status);
    
    Optional<Order> findByOrderId(String id);

}
