package de.maxpru.orderhub.service;

import de.maxpru.orderhub.domain.Product;
import de.maxpru.orderhub.exceptions.ProductNotFoundException;
import de.maxpru.orderhub.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // TODO ADD PAGINATION
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public Product findProductById(long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
    }

    public Product createProduct(Product product) {
        // FBK
        product.setId(null);
        return productRepository.save(product);
    }

    public Product updateProduct(long productId, Product changes) {
        Product foundProduct = findProductById(productId);
        foundProduct.setName(changes.getName());
        foundProduct.setPrice(changes.getPrice());
        foundProduct.setStock(changes.getStock());
        return productRepository.save(foundProduct);
    }

    public void deleteProductById(long id) {
        Product existing = findProductById(id);
        productRepository.delete(existing);
    }

}
