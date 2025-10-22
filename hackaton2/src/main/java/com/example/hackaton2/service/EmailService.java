package com.example.hackaton2.service;

import com.example.hackaton2.auth.SalesAggregates;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:oreo.bot@example.com}")
    private String from;

    public void sendSummary(String to, String summary, SalesAggregates agg) {
        if (to == null || to.isBlank()) throw new IllegalArgumentException("emailTo es obligatorio");
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject("Reporte Semanal Oreo");
            String html = """
                    <html><body>
                    <h2>🍪 Reporte Semanal Oreo</h2>
                    <p>%s</p>
                    <hr/>
                    <p><b>Total unidades:</b> %d<br/>
                    <b>Total revenue:</b> $%.2f<br/>
                    <b>Top SKU:</b> %s<br/>
                    <b>Top Branch:</b> %s</p>
                    </body></html>
                    """.formatted(summary, agg.getTotalUnits(), agg.getTotalRevenue(),
                    nvl(agg.getTopSku()), nvl(agg.getTopBranch()));
            helper.setText(html, true);
            mailSender.send(msg);
        } catch (Exception e) {
            throw new RuntimeException("Email no disponible: " + e.getMessage());
        }
    }

    private String nvl(String s) { return (s == null ? "N/A" : s); }
}