package io.github.brunoyillli.ordermanager.service;

import io.github.brunoyillli.ordermanager.dto.OrderDTO;
import io.github.brunoyillli.ordermanager.dto.ProductDTO;
import io.github.brunoyillli.ordermanager.entity.Order;
import io.github.brunoyillli.ordermanager.exception.OrderException;
import io.github.brunoyillli.ordermanager.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderProcessingServiceTest {

    @InjectMocks
    private OrderProcessingService orderProcessingService;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        OrderDTO orderDTO = createOrderDTO();
        when(orderRepository.findByOrderId(orderDTO.getOrderId())).thenReturn(Optional.empty());

        orderProcessingService.processOrder(orderDTO);

        verify(orderRepository, times(1)).save(any(Order.class));
    }
    
    @Test
    void testSaveErrorStatus() {
        OrderDTO orderDTO = createOrderDTO();
        orderDTO.setStatus("ERROR");
        when(orderRepository.findByOrderId(orderDTO.getOrderId())).thenReturn(Optional.empty());

        orderProcessingService.processOrder(orderDTO);

        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testNullException() {
        OrderDTO orderDTO = createOrderDTO();
        orderDTO.setOrderId(null);

        assertThrows(NullPointerException.class, () -> orderProcessingService.processOrder(orderDTO));

        verify(orderRepository, never()).save(any(Order.class));
    }
    
    @Test
    void testNullIdException() {
        OrderDTO orderDTO = createOrderDTO();
        orderDTO.setOrderId("");

        assertThrows(OrderException.class, () -> orderProcessingService.processOrder(orderDTO));

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testOrderAlreadyExistsException() {
        OrderDTO orderDTO = createOrderDTO();
        when(orderRepository.findByOrderId(orderDTO.getOrderId())).thenReturn(Optional.of(new Order()));

        OrderException exception = assertThrows(OrderException.class, () -> orderProcessingService.processOrder(orderDTO));
        assertEquals("Order with ID " + orderDTO.getOrderId() + " already exists in database.", exception.getMessage());

        verify(orderRepository, never()).save(any(Order.class));
    }

    private OrderDTO createOrderDTO() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId("12345");
        orderDTO.setCustomerId(1);
        orderDTO.setStatus("PROCESSING");

        ProductDTO product1 = new ProductDTO();
        product1.setProductId("prod-001");
        product1.setProductName("Product A");
        product1.setQuantity(2);
        product1.setPrice(BigDecimal.valueOf(50.75));

        ProductDTO product2 = new ProductDTO();
        product2.setProductId("prod-002");
        product2.setProductName("Product B");
        product2.setQuantity(1);
        product2.setPrice(BigDecimal.valueOf(150.50));

        orderDTO.setProducts(List.of(product1, product2));
        return orderDTO;
    }
}
