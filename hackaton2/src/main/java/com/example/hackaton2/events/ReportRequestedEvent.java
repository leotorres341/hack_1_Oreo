package com.example.hackaton2.events;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReportRequestedEvent {
    private String emailTo;
    private LocalDateTime from;
    private LocalDateTime to;
    private String branch;
}

