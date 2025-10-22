package com.example.hackaton2.auth;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalesAggregates {
    private int totalUnits;
    private double totalRevenue;
    private String topSku;
    private String topBranch;
}
