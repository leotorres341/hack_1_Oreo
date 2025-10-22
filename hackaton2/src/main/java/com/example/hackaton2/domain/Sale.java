package com.example.hackaton2.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sale {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String sku;

    @Column(nullable = false)
    private int units;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private String branch;

    @Column(nullable = false)
    private LocalDateTime soldAt;

    @Column(nullable = false)
    private String createdBy;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
