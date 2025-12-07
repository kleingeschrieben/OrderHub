package de.maxpru.orderhub.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class OrderNotFoundException extends OrderHubException {
    private final Long orderId;

    public OrderNotFoundException(Long orderId) {
        // TODO MOVE TEXT TO CONST FILE
        super(String.format("Order with id %d not found", orderId), "ORDER_NOT_FOUND", HttpStatus.NOT_FOUND);
        this.orderId = orderId;
    }
}
