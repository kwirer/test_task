package com.example.test.service;

import com.example.test.entities.Match;
import com.example.test.protocol.response.MatchResponse;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface MatchService {
    MatchResponse get(UUID matchId);

    Match play(Match match, UUID firstParticipant, UUID secondParticipant);

    Map<Integer, Set<UUID>> getMatchIds(UUID tournamentId);

    Map<Integer, Set<Match>> getMatches(UUID tournamentId);

    void save(List<Match> matches);
}