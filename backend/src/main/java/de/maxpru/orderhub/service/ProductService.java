package de.maxpru.orderhub.service;

import de.maxpru.orderhub.domain.Product;
import de.maxpru.orderhub.exceptions.ProductNotFoundException;
import de.maxpru.orderhub.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    private static final int MAX_PAGE_SIZE = 50;
    private static final int DEFAULT_PAGE_SIZE = 10;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<Product> findAllProducts(int page, int size) {
        if (page < 0) page = 0;
        if (size < 1) size = DEFAULT_PAGE_SIZE;
        else if (size > MAX_PAGE_SIZE) size = MAX_PAGE_SIZE;

        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAll(pageable);
    }

    public Product findProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
    }

    public Product createProduct(Product product) {
        // FBK
        product.setId(null);
        return productRepository.save(product);
    }

    public Product updateProduct(Long productId, Product changes) {
        Product foundProduct = findProductById(productId);
        foundProduct.setName(changes.getName());
        foundProduct.setPrice(changes.getPrice());
        foundProduct.setStock(changes.getStock());
        return productRepository.save(foundProduct);
    }

    public void updateStock(Long productId, int stock) {
        Product foundProduct = findProductById(productId);
        foundProduct.setStock(stock);
        productRepository.save(foundProduct);
    }

    public void deleteProductById(Long productId) {
        Product existing = findProductById(productId);
        productRepository.delete(existing);
    }

}
