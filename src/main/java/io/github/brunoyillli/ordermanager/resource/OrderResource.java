package io.github.brunoyillli.ordermanager.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.brunoyillli.ordermanager.entity.Order;
import io.github.brunoyillli.ordermanager.enums.OrderStatus;
import io.github.brunoyillli.ordermanager.service.OrderService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/orders")
public class OrderResource {

	@Autowired
	private OrderService orderService;

	@GetMapping
	public List<Order> getOrdersByStatus(
			@NotBlank(message = "Status cannot be blank") @NotNull(message = "Status cannot be null") @RequestParam String status) {
		
		List<Order> orders = new ArrayList<>();
		OrderStatus orderStatus = OrderStatus.ALL;
		try {
			orderStatus = OrderStatus.valueOf(status.toUpperCase());
		} catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid status value: " + status);
        }

		orders = orderService.getByStatus(orderStatus);
		return orders;
	}

	@GetMapping("/{id}")
	public Optional<Order> getOrderById(@NotBlank(message = "Id of Order cannot be blank") @NotNull(message = "Id of Order cannot be null")@PathVariable String id) {
		return orderService.getOrderById(id);
	}
}
