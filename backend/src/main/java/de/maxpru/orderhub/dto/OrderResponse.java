package de.maxpru.orderhub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long orderId;
    private String userId;
    private double totalPrice;
    private List<OrderItemResponse> orderItems;
}
