package io.github.brunoyillli.ordermanager.resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.github.brunoyillli.ordermanager.entity.Order;
import io.github.brunoyillli.ordermanager.enums.OrderStatus;
import io.github.brunoyillli.ordermanager.service.OrderService;

class OrderResourceTest {

    @InjectMocks
    private OrderResource orderResource;

    @Mock
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testeGetByStatus() {
        Order order = new Order();
        when(orderService.getByStatus(OrderStatus.PROCESSED)).thenReturn(List.of(order));

        List<Order> result = orderResource.getOrdersByStatus("PROCESSED");

        assertEquals(1, result.size());
        verify(orderService, times(1)).getByStatus(OrderStatus.PROCESSED);
    }

    @Test
    void testInvalidStatusException() {
        String invalidStatus = "INVALID_STATUS";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> orderResource.getOrdersByStatus(invalidStatus));
        assertEquals("Invalid status value: " + invalidStatus, exception.getMessage());

        verify(orderService, never()).getByStatus(any());
    }

    @Test
    void testGetById() {
        String orderId = "12345";
        Order order = new Order();
        when(orderService.getOrderById(orderId)).thenReturn(Optional.of(order));

        Optional<Order> result = orderResource.getOrderById(orderId);

        assertTrue(result.isPresent());
        verify(orderService, times(1)).getOrderById(orderId);
    }

    @Test
    void testGetByIdNonExist() {
        String orderId = "non-existent-id";
        when(orderService.getOrderById(orderId)).thenReturn(Optional.empty());

        Optional<Order> result = orderResource.getOrderById(orderId);

        assertFalse(result.isPresent());
        verify(orderService, times(1)).getOrderById(orderId);
    }
}
