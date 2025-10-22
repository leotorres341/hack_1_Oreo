package com.example.hackaton2.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SaleRequest {
    private String sku;
    private int units;
    private double price;
    private String branch;
    private LocalDateTime soldAt;
}
