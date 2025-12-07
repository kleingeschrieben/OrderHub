package de.maxpru.orderhub.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String userId;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true) // FBK OR
    private List<OrderItem> orderItems = new ArrayList<>();
    private double totalPrice;
}
