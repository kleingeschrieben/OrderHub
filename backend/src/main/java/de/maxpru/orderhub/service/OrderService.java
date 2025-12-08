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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

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

    public Page<Order> findAllOrders(int page, int size, Authentication authentication) {
        if (page < 0) page = 0;
        if (size < 1) size = DEFAULT_PAGE_SIZE;
        else if (size > MAX_PAGE_SIZE) size = MAX_PAGE_SIZE;

        Pageable pageable = PageRequest.of(page, size);

        String username = authentication.getName();
        boolean isAdmin = isAdmin(authentication);

        if (isAdmin) {
            return orderRepository.findAll(pageable);
        } else {
            return orderRepository.findAllByUserId(username, pageable);
        }
    }

    public Order findOrderById(Long orderId, Authentication authentication) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        String username = authentication.getName();
        if (!isAdmin(authentication) && !username.equals(order.getUserId())) {
            throw new AccessDeniedException("Not allowed to access this order");
        }
        return order;
    }

    public Order createOrder(OrderRequest request, String userId) {
        Order order = new Order();
        updateOrderFromRequest(order, request, userId);
        return orderRepository.save(order);
    }

    public Order updateOrder(Long orderId, OrderRequest request, Authentication authentication) {
        Order existing = findOrderById(orderId, authentication);
        String userId = existing.getUserId();
        updateOrderFromRequest(existing, request, userId);
        return orderRepository.save(existing);
    }

    public void deleteOrderById(Long orderId, Authentication authentication) {
        Order existing = findOrderById(orderId, authentication);
        orderRepository.delete(existing);
    }

    private void updateOrderFromRequest(Order order, OrderRequest request, String userId) {
        order.setUserId(userId);

        List<OrderItem> items = order.getOrderItems();
        items.clear();

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

        order.setTotalPrice(totalPrice);
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("ROLE_ADMIN"));
    }
}
