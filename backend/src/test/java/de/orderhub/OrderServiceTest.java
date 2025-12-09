package de.maxpru.orderhub;

import de.maxpru.orderhub.domain.Order;
import de.maxpru.orderhub.domain.OrderItem;
import de.maxpru.orderhub.domain.Product;
import de.maxpru.orderhub.dto.OrderItemRequest;
import de.maxpru.orderhub.dto.OrderRequest;
import de.maxpru.orderhub.exceptions.InsufficientStockException;
import de.maxpru.orderhub.repositories.OrderRepository;
import de.maxpru.orderhub.service.OrderService;
import de.maxpru.orderhub.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    ProductService productService;

    OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderRepository, productService);
    }

    @Test
    void createOrder_updatesItemsTotalPriceAndStock() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(10.0);
        product.setStock(5);

        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setProductId(1L);
        itemRequest.setQuantity(2);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setItems(List.of(itemRequest));

        when(productService.findProductById(1L)).thenReturn(product);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.createOrder(orderRequest, "user");

        assertEquals("user", result.getUserId());
        assertEquals(1, result.getOrderItems().size());
        assertEquals(20.0, result.getTotalPrice());

        OrderItem savedItem = result.getOrderItems().getFirst();
        assertEquals(1L, savedItem.getProduct().getId());
        assertEquals(2, savedItem.getQuantity());
        assertSame(result, savedItem.getOrder());

        verify(productService).updateStock(1L, 3);
        verify(orderRepository).save(result);
    }

    @Test
    void createOrder_throwsInsufficientStockException_whenStockTooLow() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(10.0);
        product.setStock(1);

        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setProductId(1L);
        itemRequest.setQuantity(5);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setItems(List.of(itemRequest));

        when(productService.findProductById(1L)).thenReturn(product);

        assertThrows(InsufficientStockException.class, () ->
                orderService.createOrder(orderRequest, "user"));

        verify(productService, never()).updateStock(anyLong(), anyInt());
        verify(orderRepository, never()).save(any(Order.class));
    }
}
