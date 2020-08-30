package com.example.test.service;

import com.example.test.protocol.request.ParticipantAddRequest;
import com.example.test.protocol.response.ParticipantResponse;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface ParticipantService {
    ParticipantResponse add(ParticipantAddRequest request);

    void checkParticipants(Set<UUID> participantIds);

    Set<ParticipantResponse> get(Set<UUID> participantIds);

    Map<UUID, ParticipantResponse> get(UUID... participantIds);
}