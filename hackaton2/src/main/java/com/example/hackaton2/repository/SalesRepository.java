package com.example.hackaton2.repository;


import com.example.hackaton2.domain.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface SalesRepository extends JpaRepository<Sale, UUID> {

    // Filtrar por rango de fechas (con o sin branch)
    @Query("""
           SELECT s FROM Sale s
           WHERE s.soldAt BETWEEN :from AND :to
           AND (:branch IS NULL OR s.branch = :branch)
           """)
    List<Sale> findByDateRangeAndBranch(@Param("from") LocalDateTime from,
                                        @Param("to") LocalDateTime to,
                                        @Param("branch") String branch);
}
