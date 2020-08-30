package com.example.test.protocol.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TournamentAddRequest {
    private int maxParticipants;
    private Set<UUID> participants;
}
