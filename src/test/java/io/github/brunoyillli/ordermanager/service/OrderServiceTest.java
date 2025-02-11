package io.github.brunoyillli.ordermanager.service;

import io.github.brunoyillli.ordermanager.entity.Order;
import io.github.brunoyillli.ordermanager.enums.OrderStatus;
import io.github.brunoyillli.ordermanager.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetByStatusAll() {
        Order order1 = new Order();
        Order order2 = new Order();
        when(orderRepository.findAll()).thenReturn(List.of(order1, order2));

        List<Order> orders = orderService.getByStatus(OrderStatus.ALL);

        assertEquals(2, orders.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testGetByStatusProcessed() {
        Order order = new Order();
        when(orderRepository.findByStatus(OrderStatus.PROCESSED)).thenReturn(List.of(order));

        List<Order> orders = orderService.getByStatus(OrderStatus.PROCESSED);

        assertEquals(1, orders.size());
        verify(orderRepository, times(1)).findByStatus(OrderStatus.PROCESSED);
    }

    @Test
    void testGetByOrder() {
        String orderId = "12345";
        Order order = new Order();
        when(orderRepository.findByOrderId(orderId)).thenReturn(Optional.of(order));

        Optional<Order> result = orderService.getOrderById(orderId);

        assertTrue(result.isPresent());
        verify(orderRepository, times(1)).findByOrderId(orderId);
    }

    @Test
    void testGetByOrderNotPresent() {
        String orderId = "non-existent-id";
        when(orderRepository.findByOrderId(orderId)).thenReturn(Optional.empty());

        Optional<Order> result = orderService.getOrderById(orderId);

        assertFalse(result.isPresent());
        verify(orderRepository, times(1)).findByOrderId(orderId);
    }
}
