package de.maxpru.orderhub.controller;

import de.maxpru.orderhub.domain.Order;
import de.maxpru.orderhub.dto.OrderRequest;
import de.maxpru.orderhub.dto.OrderResponse;
import de.maxpru.orderhub.mapper.OrderMapper;
import de.maxpru.orderhub.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "Operations for managing orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @Operation(summary = "List orders", description = "Returns a paginated list of orders")
    public List<OrderResponse> findAllOrders(@Parameter(description = "Page index (0-based)") @RequestParam(defaultValue = "0") int page, @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        Page<Order> orders = this.orderService.findAllOrders(page, size);
        List<OrderResponse> orderResponses = new ArrayList<>();

        for (Order order : orders) {
            orderResponses.add(OrderMapper.toOrderResponse(order));
        }

        return orderResponses;
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get order by id", description = "Returns a single order by its id")
    public OrderResponse findOrderById(@Parameter(description = "Id of the order") @PathVariable("orderId") Long orderId) {
        Order order = this.orderService.findOrderById(orderId);
        return OrderMapper.toOrderResponse(order);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create order", description = "Creates a new order for the authenticated user")
    public OrderResponse createOrder(@RequestBody @Valid OrderRequest order, Principal principal) {
        String userId = principal.getName();
        Order createdOrder = this.orderService.createOrder(order, userId);
        return OrderMapper.toOrderResponse(createdOrder);
    }

    @PutMapping("/{orderId}")
    @Operation(summary = "Update order", description = "Updates an existing order for the authenticated user")
    public OrderResponse updateOrder(@Parameter(description = "Id of the order to update") @PathVariable("orderId") Long orderId, @RequestBody @Valid OrderRequest order, Principal principal) {
        String userId = principal.getName();
        Order updatedOrder = this.orderService.updateOrder(orderId, order, userId);
        return OrderMapper.toOrderResponse(updatedOrder);
    }

    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete order", description = "Deletes an order by its id")
    public void deleteOrder(@Parameter(description = "Id of the order to delete") @PathVariable("orderId") Long orderId) {
        this.orderService.deleteOrderById(orderId);
    }
}
