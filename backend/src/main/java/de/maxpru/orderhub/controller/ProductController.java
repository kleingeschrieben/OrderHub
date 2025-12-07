package de.maxpru.orderhub.controller;

import de.maxpru.orderhub.domain.Product;
import de.maxpru.orderhub.dto.ProductRequest;
import de.maxpru.orderhub.dto.ProductResponse;
import de.maxpru.orderhub.mapper.ProductMapper;
import de.maxpru.orderhub.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Operations for managing products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // TODO ADD PAGINATION
    @GetMapping
    @Operation(summary = "List products", description = "Returns a paginated list of products")
    public List<ProductResponse> findAllProducts(@Parameter(description = "Page index (0-based)") @RequestParam(required = false, defaultValue = "0") int page,@Parameter(description = "Page size") @RequestParam(required = false, defaultValue = "20") int size) {
        Page<Product> products = this.productService.findAllProducts(page, size);
        List<ProductResponse> productResponses = new ArrayList<>();

        for (Product product : products) {
            productResponses.add(ProductMapper.toProductResponse(product));
        }

        return productResponses;
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Get product by id", description = "Returns a single product by its id")
    public ProductResponse findProductById(@Parameter(description = "Id of the product") @PathVariable("productId") Long productId) {
        Product product = this.productService.findProductById(productId);
        return ProductMapper.toProductResponse(product);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create product", description = "Creates a new product")
    public ProductResponse createProduct(@RequestBody @Valid ProductRequest product) {
        Product createdProduct = this.productService.createProduct(ProductMapper.toProduct(null, product));
        return ProductMapper.toProductResponse(createdProduct);
    }

    @PutMapping("/{productId}")
    @Operation(summary = "Update product", description = "Updates an existing product")
    public ProductResponse updateProduct(@Parameter(description = "Id of the product to update") @PathVariable("productId") Long productId, @RequestBody @Valid ProductRequest product) {
        Product productToUpdate = ProductMapper.toProduct(productId, product);
        Product updatedProduct = this.productService.updateProduct(productId, productToUpdate);
        return ProductMapper.toProductResponse(updatedProduct);
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "Delete product", description = "Deletes a product by its id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@Parameter(description = "Id of the product to delete") @PathVariable("productId") Long productId) {
        this.productService.deleteProductById(productId);
    }
}
