package com.example.test.protocol.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static java.math.BigDecimal.ZERO;

@Data
@NoArgsConstructor
@JsonInclude(NON_EMPTY)
public class MatchResponse {
    private UUID id;
    private ParticipantResponse firstParticipant;
    private ParticipantResponse secondParticipant;
    private LocalDateTime start;
    private LocalDateTime finish;
    private BigDecimal firstParticipantScore;
    private BigDecimal secondParticipantScore;
    private ParticipantResponse winner;
}
