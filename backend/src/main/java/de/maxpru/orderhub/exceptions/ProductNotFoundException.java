package de.maxpru.orderhub.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ProductNotFoundException extends OrderHubException {
    private final long productId;

    public ProductNotFoundException(long productId) {
        // TODO MOVE TEXT TO CONST FILE
        super(String.format("Product with id %d not found", productId), "PRODUCT_NOT_FOUND", HttpStatus.NOT_FOUND);
        this.productId = productId;
    }
}
