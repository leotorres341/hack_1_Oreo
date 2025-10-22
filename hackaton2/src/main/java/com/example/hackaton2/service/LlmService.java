package com.example.hackaton2.service;


import com.example.hackaton2.auth.SalesAggregates;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.naming.ServiceUnavailableException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class LlmService {

    @Value("${GITHUB_MODELS_URL}")
    private String modelsUrl;

    @Value("${MODEL_ID}")
    private String modelId;

    @Value("${GITHUB_TOKEN}")
    private String githubToken;

    private final RestTemplate restTemplate = new RestTemplate();

    public String generateSummary(SalesAggregates agg) {
        // Validaciones mínimas
        if (agg == null) throw new IllegalStateException("No hay agregados para generar resumen");

        String system = "Eres un analista que escribe resúmenes breves y claros para emails corporativos."
                + " Devuelve máximo 120 palabras, en español, sin inventar datos.";
        String user = String.format(
                "Con estos datos: totalUnits=%d, totalRevenue=%.2f, topSku=%s, topBranch=%s. " +
                "Devuelve un resumen ≤120 palabras para enviar por email.",
                agg.getTotalUnits(), agg.getTotalRevenue(), nvl(agg.getTopSku()), nvl(agg.getTopBranch())
        );

        Map<String, Object> body = Map.of(
                "model", modelId,
                "messages", List.of(
                        Map.of("role", "system", "content", system),
                        Map.of("role", "user", "content", user)
                ),
                "max_tokens", 200
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(githubToken);

        try {
            ResponseEntity<Map> resp = restTemplate.exchange(
                    modelsUrl, HttpMethod.POST, new HttpEntity<>(body, headers), Map.class
            );
            // Esperamos algo tipo { choices: [ { message: { content: "..." } } ] }
            Object content = Optional.ofNullable(resp.getBody())
                    .map(b -> ((List<Map<String, Object>>) b.get("choices")).get(0))
                    .map(choice -> ((Map<String, Object>) choice.get("message")).get("content"))
                    .orElse("Resumen no disponible.");
            String text = content.toString().trim();

            // Validación simple de 120 palabras
            if (text.split("\\s+").length > 120) {
                text = String.join(" ", Arrays.asList(text.split("\\s+")).subList(0, 120));
            }
            return text;
        } catch (Exception e) {
            throw new RuntimeException("LLM no disponible: " + e.getMessage(), e);
        }
    }

    private String nvl(String s) { return (s == null ? "N/A" : s); }
}