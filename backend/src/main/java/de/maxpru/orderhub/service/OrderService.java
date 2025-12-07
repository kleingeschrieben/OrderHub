package de.maxpru.orderhub.service;

import de.maxpru.orderhub.domain.Order;
import de.maxpru.orderhub.domain.OrderItem;
import de.maxpru.orderhub.domain.Product;
import de.maxpru.orderhub.dto.OrderItemRequest;
import de.maxpru.orderhub.dto.OrderRequest;
import de.maxpru.orderhub.exceptions.InsufficientStockException;
import de.maxpru.orderhub.exceptions.OrderNotFoundException;
import de.maxpru.orderhub.mapper.OrderMapper;
import de.maxpru.orderhub.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;

    private static final int MAX_PAGE_SIZE = 50;
    private static final int DEFAULT_PAGE_SIZE = 10;

    public OrderService(OrderRepository orderRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
    }

    public Page<Order> findAllOrders(int page, int size) {
        if (page < 0) page = 0;
        if (size < 1) size = DEFAULT_PAGE_SIZE;
        else if (size > MAX_PAGE_SIZE) size = MAX_PAGE_SIZE;

        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findAll(pageable);
    }

    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    public Order createOrder(OrderRequest request, String userId) {
        Order order = new Order();
        updateOrderFromRequest(order, request, userId);
        return orderRepository.save(order);
    }

    public Order updateOrder(Long orderId, OrderRequest request, String userId) {
        Order existing = findOrderById(orderId);
        updateOrderFromRequest(existing, request, userId);
        return orderRepository.save(existing);
    }

    private void updateOrderFromRequest(Order order, OrderRequest request, String userId) {
        order.setUserId(userId);
        order.getOrderItems().clear();

        List<OrderItem> items = new ArrayList<>();
        double totalPrice = 0.0;

        for (OrderItemRequest itemRequest : request.getItems()) {
            Product product = productService.findProductById(itemRequest.getProductId());
            if (product.getStock() < itemRequest.getQuantity()) {
                log.warn("Insufficient stock for product {}: requested {}, available {}", product.getId(), itemRequest.getQuantity(), product.getStock());
                throw new InsufficientStockException(product.getId());
            }

            int newStock = product.getStock() - itemRequest.getQuantity();
            productService.updateStock(product.getId(), newStock);

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
