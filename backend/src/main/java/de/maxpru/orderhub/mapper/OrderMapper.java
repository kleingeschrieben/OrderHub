package de.maxpru.orderhub.mapper;

import de.maxpru.orderhub.domain.Order;
import de.maxpru.orderhub.domain.OrderItem;
import de.maxpru.orderhub.domain.Product;
import de.maxpru.orderhub.dto.OrderItemRequest;
import de.maxpru.orderhub.dto.OrderItemResponse;
import de.maxpru.orderhub.dto.OrderResponse;

import java.util.ArrayList;
import java.util.List;

public class OrderMapper {

    public static OrderItem toOrderItem(Product product, OrderItemRequest request) {
        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(request.getQuantity());
        item.setPrice(product.getPrice() * request.getQuantity());
        return item;
    }

    public static OrderResponse toOrderResponse(Order order) {
        List<OrderItemResponse> itemResponses = new ArrayList<>();
        for (OrderItem item : order.getOrderItems()) {
            itemResponses.add(toOrderItemResponse(item));
        }
        return new OrderResponse(
                order.getId(),
                order.getUserId(),
                order.getTotalPrice(),
                itemResponses
        );
    }

    public static OrderItemResponse toOrderItemResponse(OrderItem item) {
        Product product = item.getProduct();
        return new OrderItemResponse(
                product.getId(),
                product.getName(),
                item.getQuantity(),
                item.getPrice()
        );
    }
}
