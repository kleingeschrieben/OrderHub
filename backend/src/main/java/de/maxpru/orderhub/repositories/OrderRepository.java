package de.maxpru.orderhub.repositories;

import de.maxpru.orderhub.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByUserId(String userId, Pageable pageable);
}
