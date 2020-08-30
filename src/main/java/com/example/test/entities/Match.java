package com.example.test.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static java.math.BigDecimal.ZERO;
import static java.util.Objects.isNull;

@Data
@Entity
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Match extends BaseEntity {

    private UUID tournamentId;

    private UUID firstParticipant;

    private UUID secondParticipant;

    private BigDecimal firstParticipantScore = ZERO;

    private BigDecimal secondParticipantScore = ZERO;

    private LocalDateTime start;

    private LocalDateTime finish;

    private int stage;

    public Match(UUID tournamentId, int stage) {
        this.tournamentId = tournamentId;
        this.stage = stage;
    }

    public UUID getWinner() {
        if (isNull(firstParticipant)) {
            return secondParticipant;
        }

        if (isNull(secondParticipant)) {
            return firstParticipant;
        }

        return firstParticipantScore.compareTo(secondParticipantScore) >= 0 ? firstParticipant : secondParticipant;
    }
}