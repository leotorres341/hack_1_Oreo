package com.example.hackaton2.events;



import com.example.hackaton2.auth.SalesAggregationService;
import com.example.hackaton2.auth.SalesAggregates;
import com.example.hackaton2.service.EmailService;
import com.example.hackaton2.service.LlmService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class ReportEventListener {

    private final SalesAggregationService aggService;
    private final LlmService llmService;
    private final EmailService emailService;

    @Async
    @EventListener
    public void handleReport(ReportRequestedEvent event) {
        SalesAggregates agg = aggService.calculateAggregates(
                event.getFrom(),
                event.getTo(),
                event.getBranch()
        );

        String summary = llmService.generateSummary(agg);
        emailService.sendSummary(event.getEmailTo(), summary, agg);
    }
}
