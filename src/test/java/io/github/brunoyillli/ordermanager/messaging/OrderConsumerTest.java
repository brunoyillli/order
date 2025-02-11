package io.github.brunoyillli.ordermanager.messaging;

import io.github.brunoyillli.ordermanager.dto.OrderDTO;
import io.github.brunoyillli.ordermanager.service.OrderProcessingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class OrderConsumerTest {

    @InjectMocks
    private OrderConsumer orderConsumer;

    @Mock
    private OrderProcessingService orderProcessingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void receiveOrder_shouldProcessOrder_whenOrderIsReceived() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId("12345");

        orderConsumer.receiveOrder(orderDTO);

        verify(orderProcessingService, times(1)).processOrder(orderDTO);
    }

   
}
