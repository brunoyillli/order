package io.github.brunoyillli.ordermanager.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.brunoyillli.ordermanager.entity.Order;
import io.github.brunoyillli.ordermanager.enums.OrderStatus;
import io.github.brunoyillli.ordermanager.repository.OrderRepository;

@Service
public class OrderService {
	@Autowired
    private OrderRepository orderRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    public List<Order> getByStatus(OrderStatus status) {
    	if(status.equals(OrderStatus.ALL)) {
    		LOGGER.info("Searching all orders");
    		return orderRepository.findAll();
    	}
    	LOGGER.info("Searching by status {} orders", status.name());
        return orderRepository.findByStatus(status);
    }

    public Optional<Order> getOrderById(String id) {
    	LOGGER.info("Searching order by ID {}", id);
        return orderRepository.findByOrderId(id);
    }
}
