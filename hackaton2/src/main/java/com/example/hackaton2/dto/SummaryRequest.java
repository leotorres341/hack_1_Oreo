package com.example.hackaton2.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SummaryRequest {
    private String branch;     // obligatorio para BRANCH; opcional o cualquiera para CENTRAL
    private String emailTo;    // obligatorio
    private LocalDate from;    // opcional
    private LocalDate to;      // opcional
    // Bonus premium
    private Boolean includeCharts;
    private Boolean attachPdf;
    private String format;     // "PREMIUM" opcional
}