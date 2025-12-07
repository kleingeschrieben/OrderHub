package de.maxpru.orderhub.mapper;

import de.maxpru.orderhub.domain.Product;
import de.maxpru.orderhub.dto.ProductRequest;
import de.maxpru.orderhub.dto.ProductResponse;

public class ProductMapper {

    public static ProductResponse toProductResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getStock());
    }

    public static Product toProduct(Long productId, ProductRequest productResponse) {
        Product product = new Product();
        product.setId(productId);
        product.setName(productResponse.getName());
        product.setPrice(productResponse.getPrice());
        product.setStock(productResponse.getStock());
        return product;
    }
}
