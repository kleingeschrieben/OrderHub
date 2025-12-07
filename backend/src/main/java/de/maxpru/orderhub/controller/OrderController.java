package de.maxpru.orderhub.controller;

import de.maxpru.orderhub.domain.Order;
import de.maxpru.orderhub.dto.OrderRequest;
import de.maxpru.orderhub.dto.OrderResponse;
import de.maxpru.orderhub.mapper.OrderMapper;
import de.maxpru.orderhub.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // TODO ADD PAGINATION
    @GetMapping
    public List<OrderResponse> findAllOrders() {
        List<Order> orders = this.orderService.findAllOrders();
        List<OrderResponse> orderResponses = new ArrayList<>();

        for (Order order : orders) {
            orderResponses.add(OrderMapper.toOrderResponse(order));
        }

        return orderResponses;
    }

    @GetMapping("/{orderId}")
    public OrderResponse findOrderById(@PathVariable("orderId") Long orderId) {
        Order order = this.orderService.findOrderById(orderId);
        return OrderMapper.toOrderResponse(order);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(@RequestBody @Valid OrderRequest order) {
        Order createdOrder = this.orderService.createOrder(order);
        return OrderMapper.toOrderResponse(createdOrder);
    }

    @PutMapping("/{orderId}")
    public OrderResponse updateOrder(@PathVariable("orderId") Long orderId, @RequestBody @Valid OrderRequest order) {
        Order updatedOrder = this.orderService.updateOrder(orderId, order);
        return OrderMapper.toOrderResponse(updatedOrder);
    }

    @DeleteMapping("/{orderId}")
    public void deleteOrder(@PathVariable("orderId") Long orderId) {
        this.orderService.deleteOrderById(orderId);
    }
}
