package io.github.brunoyillli.ordermanager.dto;

import java.util.List;

public class OrderDTO {
	private String orderId;
    private int customerId;
    private String status;
    private List<ProductDTO> products;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<ProductDTO> getProducts() {
		return products;
	}
	public void setProducts(List<ProductDTO> products) {
		this.products = products;
	}
    
    
}
