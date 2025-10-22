package com.example.hackaton2.service;

import com.example.hackaton2.domain.Sale;
import com.example.hackaton2.dto.SaleRequest;
import com.example.hackaton2.dto.SaleResponse;
import com.example.hackaton2.repository.SalesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesService {

    private final SalesRepository salesRepository;

    public SaleResponse createSale(SaleRequest req, Authentication auth, String userBranch, String role) {
        if (role.equals("ROLE_BRANCH") && !userBranch.equals(req.getBranch()))
            throw new RuntimeException("No puedes crear ventas para otra sucursal");

        Sale sale = Sale.builder()
                .sku(req.getSku())
                .units(req.getUnits())
                .price(req.getPrice())
                .branch(req.getBranch())
                .soldAt(req.getSoldAt())
                .createdBy(auth.getName())
                .build();
        salesRepository.save(sale);
        return SaleResponse.builder()
                .id(sale.getId())
                .sku(sale.getSku())
                .units(sale.getUnits())
                .price(sale.getPrice())
                .branch(sale.getBranch())
                .soldAt(sale.getSoldAt())
                .createdBy(sale.getCreatedBy())
                .build();
    }

    public List<SaleResponse> listSales(String branch, String role) {
        List<Sale> sales = salesRepository.findAll();
        if (role.equals("ROLE_BRANCH")) {
            sales = sales.stream().filter(s -> s.getBranch().equals(branch)).collect(Collectors.toList());
        }
        return sales.stream()
                .map(s -> new SaleResponse(s.getId(), s.getSku(), s.getUnits(), s.getPrice(), s.getBranch(), s.getSoldAt(), s.getCreatedBy()))
                .collect(Collectors.toList());
    }

    public SaleResponse getSaleById(UUID id, String branch, String role) {
        Sale s = salesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        if (role.equals("ROLE_BRANCH") && !s.getBranch().equals(branch))
            throw new RuntimeException("No puedes ver ventas de otra sucursal");
        return new SaleResponse(s.getId(), s.getSku(), s.getUnits(), s.getPrice(), s.getBranch(), s.getSoldAt(), s.getCreatedBy());
    }

    public void deleteSale(UUID id) {
        salesRepository.deleteById(id);
    }
}
