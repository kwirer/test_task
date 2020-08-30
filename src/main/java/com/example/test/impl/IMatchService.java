package com.example.test.impl;


import com.example.test.entities.Match;
import com.example.test.exception.ApiException;
import com.example.test.protocol.response.MatchResponse;
import com.example.test.protocol.response.ParticipantResponse;
import com.example.test.repository.MatchRepository;
import com.example.test.service.MatchService;
import com.example.test.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.lambda.tuple.Tuple2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.example.test.protocol.IErrorCode.INVALID_MATCH_ID;
import static com.example.test.utils.CommonUtils.generateRandomBigDecimal;
import static java.util.Objects.nonNull;
import static org.jooq.lambda.Seq.seq;

@Slf4j
@Service
@RequiredArgsConstructor
public class IMatchService implements MatchService {

    private final ModelMapper modelMapper;

    private final MatchRepository matchRepository;

    private final ParticipantService participantService;

    @Override
    public MatchResponse get(UUID matchId) {
        return matchRepository
                .findById(matchId)
                .map(match -> {
                    MatchResponse map = modelMapper.map(match, MatchResponse.class);
                    Map<UUID, ParticipantResponse> participantsMap = participantService.get(match.getFirstParticipant(), match.getSecondParticipant(), match.getWinner());

                    map.setFirstParticipant(participantsMap.get(match.getFirstParticipant()));
                    map.setSecondParticipant(participantsMap.get(match.getSecondParticipant()));
                    map.setWinner(participantsMap.get(match.getWinner()));

                    return map;
                })
                .orElseThrow(() -> new ApiException(INVALID_MATCH_ID));
    }

    @Override
    public Match play(Match match, UUID firstParticipant, UUID secondParticipant) {
        match.setStart(LocalDateTime.now());
        match.setFinish(match.getStart().plusHours(1));

        match.setFirstParticipant(firstParticipant);
        match.setSecondParticipant(secondParticipant);

        if (nonNull(firstParticipant) && nonNull(secondParticipant)) {
            match.setFirstParticipantScore(generateRandomBigDecimal());
            match.setSecondParticipantScore(generateRandomBigDecimal());
        }

        return matchRepository.save(match);
    }

    @Override
    public Map<Integer, Set<UUID>> getMatchIds(UUID tournamentId) {
        return seq(getMatches(tournamentId))
                .toMap(Tuple2::v1, x -> seq(x.v2()).map(Match::getId).toSet());
    }

    @Override
    public Map<Integer, Set<Match>> getMatches(UUID tournamentId) {
        return seq(matchRepository.findAllByTournamentId(tournamentId))
                .sorted(Match::getStage)
                .reverse()
                .grouped(Match::getStage)
                .toMap(Tuple2::v1, x -> x.v2().toSet());
    }

    @Override
    public void save(List<Match> matches) {
        matchRepository.saveAll(matches);
    }
}