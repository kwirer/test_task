package com.example.test.service;

import com.example.test.protocol.request.TournamentAddRequest;
import com.example.test.protocol.response.TournamentResponse;

import java.util.UUID;

public interface TournamentService {
    TournamentResponse add(TournamentAddRequest request);

    TournamentResponse get(UUID tournamentId);

    TournamentResponse addParticipant(UUID tournamentId, UUID participantId);

    TournamentResponse deleteParticipant(UUID tournamentId, UUID participantId);

    TournamentResponse start(UUID tournamentId);
}