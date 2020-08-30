package com.example.test.protocol.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@Data
@NoArgsConstructor
@JsonInclude(NON_EMPTY)
public class TournamentResponse {
    private UUID id;
    private Set<ParticipantResponse> participants;
    private int maxParticipants;

    private UUID winner;
    private List<Stage> stages;

    @Data
    @NoArgsConstructor
    public static class Stage {
        @JsonIgnore
        private int stageNumber;
        private String stage;
        private Set<UUID> matches;

        public Stage(int stageNumber, String stage, Set<UUID> matches) {
            this.stageNumber = stageNumber;
            this.stage = stage;
            this.matches = matches;
        }
    }
}
