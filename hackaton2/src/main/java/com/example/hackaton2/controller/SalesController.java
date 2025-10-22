package com.example.hackaton2.controller;
import com.example.hackaton2.dto.SaleRequest;
import com.example.hackaton2.dto.SaleResponse;
import com.example.hackaton2.service.SalesService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor
public class SalesController {

    private final SalesService salesService;

    @PostMapping
    public ResponseEntity<SaleResponse> create(@RequestBody SaleRequest req, Authentication auth) {
        String role = auth.getAuthorities().iterator().next().getAuthority();
        String branch = (String) auth.getDetails();
        SaleResponse resp = salesService.createSale(req, auth, branch, role);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping
    public ResponseEntity<List<SaleResponse>> list(Authentication auth) {
        String role = auth.getAuthorities().iterator().next().getAuthority();
        String branch = (String) auth.getDetails();
        return ResponseEntity.ok(salesService.listSales(branch, role));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleResponse> getById(@PathVariable UUID id, Authentication auth) {
        String role = auth.getAuthorities().iterator().next().getAuthority();
        String branch = (String) auth.getDetails();
        return ResponseEntity.ok(salesService.getSaleById(id, branch, role));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        salesService.deleteSale(id);
        return ResponseEntity.noContent().build();
    }
}
