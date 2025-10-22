package com.example.hackaton2.auth;


import com.example.hackaton2.domain.Sale;
import com.example.hackaton2.repository.SalesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesAggregationService {

    private final SalesRepository repo;

    public SalesAggregates calculateAggregates(LocalDateTime from, LocalDateTime to, String branch) {
        List<Sale> sales = repo.findByDateRangeAndBranch(from, to, branch);
        if (sales.isEmpty()) return new SalesAggregates(0, 0, null, null);

        int totalUnits = sales.stream().mapToInt(Sale::getUnits).sum();
        double totalRevenue = sales.stream().mapToDouble(s -> s.getUnits() * s.getPrice()).sum();

        String topSku = sales.stream()
                .collect(Collectors.groupingBy(Sale::getSku, Collectors.summingInt(Sale::getUnits)))
                .entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();

        String topBranch = sales.stream()
                .collect(Collectors.groupingBy(Sale::getBranch, Collectors.summingInt(Sale::getUnits)))
                .entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();

        return new SalesAggregates(totalUnits, totalRevenue, topSku, topBranch);
    }
}
