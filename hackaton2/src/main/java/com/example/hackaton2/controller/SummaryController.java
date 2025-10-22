package com.example.hackaton2.controller;


import com.example.hackaton2.dto.SummaryRequest;
import com.example.hackaton2.events.ReportRequestedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.*;

@RestController
@RequestMapping("/sales/summary")
@RequiredArgsConstructor
public class SummaryController {

    private final ApplicationEventPublisher publisher;

    @PostMapping("/weekly")
    public ResponseEntity<?> requestWeekly(@RequestBody SummaryRequest req, Authentication auth) {
        String role = auth.getAuthorities().iterator().next().getAuthority();
        String userBranch = (String) auth.getDetails();

        if (req.getEmailTo() == null || req.getEmailTo().isBlank())
            throw new IllegalArgumentException("emailTo es obligatorio");

        // Para usuarios BRANCH, forzamos su branch
        if ("ROLE_BRANCH".equals(role)) {
            req.setBranch(userBranch);
        }

        // Si no hay fechas, última semana [hoy-7, hoy]
        LocalDate toDate = req.getTo() != null ? req.getTo() : LocalDate.now(ZoneId.of("America/Lima"));
        LocalDate fromDate = req.getFrom() != null ? req.getFrom() : toDate.minusDays(7);

        var event = new ReportRequestedEvent(
                req.getEmailTo(),
                fromDate.atStartOfDay(),
                toDate.atTime(23,59,59),
                req.getBranch()
        );
        publisher.publishEvent(event);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                """
                {
                  "requestId": "req_%d",
                  "status": "PROCESSING",
                  "message": "Su solicitud de reporte está siendo procesada. Recibirá el resumen en %s en unos momentos.",
                  "estimatedTime": "30-60 segundos",
                  "requestedAt": "%s"
                }
                """.formatted(System.currentTimeMillis(), req.getEmailTo(), Instant.now().toString())
        );
    }

    // Bonus opcional
    @PostMapping("/weekly/premium")
    public ResponseEntity<?> requestWeeklyPremium(@RequestBody SummaryRequest req, Authentication auth) {
        // Igual que el anterior; aquí podrías setear flags de includeCharts/attachPdf/format en el evento
        return requestWeekly(req, auth);
    }
}