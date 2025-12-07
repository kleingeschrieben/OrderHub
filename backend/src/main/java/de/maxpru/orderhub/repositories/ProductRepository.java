package de.maxpru.orderhub.repositories;

import de.maxpru.orderhub.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> { }
