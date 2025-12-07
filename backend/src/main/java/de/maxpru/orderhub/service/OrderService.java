package de.maxpru.orderhub.service;

import de.maxpru.orderhub.domain.Order;
import de.maxpru.orderhub.domain.OrderItem;
import de.maxpru.orderhub.domain.Product;
import de.maxpru.orderhub.dto.OrderItemRequest;
import de.maxpru.orderhub.dto.OrderRequest;
import de.maxpru.orderhub.exceptions.OrderNotFoundException;
import de.maxpru.orderhub.mapper.OrderMapper;
import de.maxpru.orderhub.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;

    public OrderService(OrderRepository orderRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
    }

    // TODO ADD PAGINATION
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    public Order createOrder(OrderRequest request) {
        Order order = new Order();
        updateOrderFromRequest(order, request);
        return orderRepository.save(order);
    }

    public Order updateOrder(Long orderId, OrderRequest request) {
        Order existing = findOrderById(orderId);
        updateOrderFromRequest(existing, request);
        return orderRepository.save(existing);
    }

    private void updateOrderFromRequest(Order order, OrderRequest request) {
        order.setUserId(request.getUserId());
        order.getOrderItems().clear();

        List<OrderItem> items = new ArrayList<>();
        double totalPrice = 0.0;

        for (OrderItemRequest itemRequest : request.getItems()) {
            Product product = productService.findProductById(itemRequest.getProductId());
            OrderItem item = OrderMapper.toOrderItem(product, itemRequest);
            item.setOrder(order);
            items.add(item);
            totalPrice += item.getPrice();
        }

        order.setOrderItems(items);
        order.setTotalPrice(totalPrice);
    }

    public void deleteOrderById(Long orderId) {
        Order existing = findOrderById(orderId);
        orderRepository.delete(existing);
    }
}
