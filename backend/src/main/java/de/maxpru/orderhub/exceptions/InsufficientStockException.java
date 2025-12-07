package de.maxpru.orderhub.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class InsufficientStockException extends OrderHubException {
    private final Long productId;

    public InsufficientStockException(Long productId) {
        // TODO MOVE TEXT TO CONST FILE
        super(String.format("Insufficient stock for product with id %d", productId), "INSUFFICIENT_STOCK", HttpStatus.CONFLICT);
        this.productId = productId;
    }
}
