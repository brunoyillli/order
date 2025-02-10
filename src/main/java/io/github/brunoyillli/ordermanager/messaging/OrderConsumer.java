package io.github.brunoyillli.ordermanager.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import io.github.brunoyillli.ordermanager.dto.OrderDTO;
import io.github.brunoyillli.ordermanager.service.OrderProcessingService;

@Component
public class OrderConsumer {
	
	@Autowired
	private OrderProcessingService orderProcessingService;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderProcessingService.class);

	
    @JmsListener(destination = "order-queue")
    public void receiveOrder(OrderDTO order) {
    	LOGGER.info("Order received: ()", order.getOrderId());
        orderProcessingService.processOrder(order);
    }
}
