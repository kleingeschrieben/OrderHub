package de.maxpru.orderhub.controller;

import de.maxpru.orderhub.domain.Product;
import de.maxpru.orderhub.dto.ProductRequest;
import de.maxpru.orderhub.dto.ProductResponse;
import de.maxpru.orderhub.mapper.ProductMapper;
import de.maxpru.orderhub.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // TODO ADD PAGINATION
    @GetMapping
    public List<ProductResponse> findAllProducts() {
        List<Product> products = this.productService.findAllProducts();
        List<ProductResponse> productResponses = new ArrayList<>();

        for (Product product : products) {
            productResponses.add(ProductMapper.toProductResponse(product));
        }

        return productResponses;
    }

    @GetMapping("/{productId}")
    public ProductResponse findProductById(@PathVariable("productId") long productId) {
        Product product = this.productService.findProductById(productId);
        return ProductMapper.toProductResponse(product);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@RequestBody ProductRequest product) {
        Product createdProduct = this.productService.createProduct(ProductMapper.toProduct(null, product));
        return ProductMapper.toProductResponse(createdProduct);
    }

    @PutMapping("/{productId}")
    public ProductResponse updateProduct(@PathVariable("productId") long productId, @RequestBody ProductRequest product) {
        Product productToUpdate = ProductMapper.toProduct(productId, product);
        Product updatedProduct = this.productService.updateProduct(productId, productToUpdate);
        return ProductMapper.toProductResponse(updatedProduct);
    }

    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable("productId") long productId) {
        this.productService.deleteProductById(productId);
    }
}
