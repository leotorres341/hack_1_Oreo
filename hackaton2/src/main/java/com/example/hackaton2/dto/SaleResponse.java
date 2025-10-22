package com.example.hackaton2.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleResponse {
    private UUID id;
    private String sku;
    private int units;
    private double price;
    private String branch;
    private LocalDateTime soldAt;
    private String createdBy;
}
