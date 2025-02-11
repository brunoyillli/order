package io.github.brunoyillli.ordermanager.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.brunoyillli.ordermanager.dto.OrderDTO;
import io.github.brunoyillli.ordermanager.entity.Order;
import io.github.brunoyillli.ordermanager.entity.Product;
import io.github.brunoyillli.ordermanager.enums.OrderStatus;
import io.github.brunoyillli.ordermanager.exception.OrderException;
import io.github.brunoyillli.ordermanager.repository.OrderRepository;

@Service
public class OrderProcessingService {
	
	@Autowired
	private OrderRepository orderRepository;
	
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderProcessingService.class);

	
	public void processOrder(OrderDTO orderDTO) {
		verifyOrder(orderDTO);
		
		BigDecimal totalValue = calculateTotalValue(orderDTO);
		
        OrderStatus finalStatus = analyzeStatus(orderDTO.getStatus());

		Order order = new Order();
        order.setOrderId(orderDTO.getOrderId());
        order.setCustomerId(orderDTO.getCustomerId());
        order.setStatus(finalStatus);
        order.setTotalValue(totalValue);

        List<Product> products = orderDTO.getProducts().stream().map(productDTO -> {
            Product product = new Product();
            product.setProductId(productDTO.getProductId());
            product.setProductName(productDTO.getProductName());
            product.setQuantity(productDTO.getQuantity());
            product.setPrice(productDTO.getPrice());
            return product;
        }).collect(Collectors.toList());

        order.setProducts(products);
        orderRepository.save(order);

        LOGGER.info("Order {} processed and saved successfully!", orderDTO.getOrderId());
    }

	private void verifyOrder(OrderDTO orderDTO) {
		if(orderDTO.getOrderId() == null | orderDTO.getOrderId().isBlank()) {
	         throw new OrderException("Order ID is null ");
		}
		 Optional<Order> existingOrder = orderRepository.findByOrderId(orderDTO.getOrderId());
	     if (existingOrder.isPresent()) {
	         throw new OrderException("Order with ID " + orderDTO.getOrderId() + " already exists in database.");
	     }
	}

	private BigDecimal calculateTotalValue(OrderDTO orderDTO) {
		BigDecimal totalValue = orderDTO.getProducts().stream()
                .map(product -> product.getPrice().multiply(BigDecimal.valueOf(product.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
		return totalValue;
	}
	
	private OrderStatus analyzeStatus(String status) {
        if (status != null && status.toLowerCase().contains("erro")) {
            return OrderStatus.ERROR;
        }
        return OrderStatus.PROCESSED;
    }
}
