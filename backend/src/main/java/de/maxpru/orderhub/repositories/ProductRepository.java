package de.maxpru.orderhub.repositories;

import de.maxpru.orderhub.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByDeletedFalse(Pageable pageable);
    Page<Product> findAllByDeletedFalseAndNameContainingIgnoreCase(String name, Pageable pageable);
    Optional<Product> findByIdAndDeletedFalse(Long id);
}
