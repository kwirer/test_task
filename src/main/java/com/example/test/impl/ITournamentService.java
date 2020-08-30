package com.example.test.impl;

import com.example.test.entities.Match;
import com.example.test.entities.Tournament;
import com.example.test.exception.ApiException;
import com.example.test.protocol.request.TournamentAddRequest;
import com.example.test.protocol.response.TournamentResponse;
import com.example.test.repository.TournamentRepository;
import com.example.test.service.MatchService;
import com.example.test.service.ParticipantService;
import com.example.test.service.TournamentService;
import com.example.test.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.jooq.lambda.Seq;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

import static com.example.test.protocol.IErrorCode.*;
import static com.example.test.utils.CommonUtils.getStageName;
import static com.example.test.utils.CommonUtils.validateByPredicate;
import static java.lang.Boolean.TRUE;
import static java.lang.Math.pow;
import static java.util.Collections.shuffle;
import static java.util.function.Predicate.not;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.jooq.lambda.Seq.range;
import static org.jooq.lambda.Seq.seq;

@Slf4j
@Service
@RequiredArgsConstructor
public class ITournamentService implements TournamentService {

    private final ModelMapper modelMapper;

    private final MatchService matchService;

    private final ParticipantService participantService;

    private final TournamentRepository tournamentRepository;

    @Override
    @Transactional
    public TournamentResponse add(TournamentAddRequest request) {
        validateByPredicate(request.getMaxParticipants(), x -> x < 4, INVALID_MAX_PARTICIPANTS_COUNT);
        validateByPredicate(request.getMaxParticipants(), not(CommonUtils::isPowerOfTwo), INVALID_MAX_PARTICIPANTS_COUNT);

        Tournament tournament = new Tournament();

        tournament.setMaxParticipants(request.getMaxParticipants());

        if (isNotEmpty(request.getParticipants())) {
            validateByPredicate(tournament.getMaxParticipants(), maxSize -> maxSize != request.getParticipants().size(), INVALID_PARTICIPANTS_COUNT);
            participantService.checkParticipants(request.getParticipants());

            tournament.setParticipants(request.getParticipants());
        }

        tournamentRepository.save(tournament);

        int stagesCount = CommonUtils.log2(tournament.getMaxParticipants());

        matchService.save(
                range(0, stagesCount)
                        .map(stage -> range(0, (int) pow(2, stage)).map(x -> new Match(tournament.getId(), stage)).toList())
                        .flatMap(Seq::seq)
                        .toList());

        return map(tournamentRepository.save(tournament));
    }

    @Override
    public TournamentResponse get(UUID tournamentId) {
        return tournamentRepository
                .findById(tournamentId)
                .map(this::map)
                .orElseThrow(() -> new ApiException(INVALID_TOURNAMENT_ID));
    }

    @Override
    public TournamentResponse addParticipant(UUID tournamentId, UUID participantId) {
        return tournamentRepository
                .findById(tournamentId)
                .map(tournament -> {
                    validateByPredicate(tournament, Tournament::isFinished, TOURNAMENT_IS_FINISHED);
                    validateByPredicate(participantId, tournament.getParticipants()::contains, ALREADY_PARTICIPANT);

                    validateByPredicate(
                            tournament.getParticipants().size() + 1 > tournament.getMaxParticipants(),
                            TRUE::equals,
                            MAX_PARTICIPANTS_REACHED);

                    participantService.checkParticipants(Set.of(participantId));
                    tournament.getParticipants().add(participantId);

                    return tournamentRepository.save(tournament);
                })
                .map(this::map)
                .orElseThrow(() -> new ApiException(INVALID_TOURNAMENT_ID));
    }

    @Override
    public TournamentResponse deleteParticipant(UUID tournamentId, UUID participantId) {
        return tournamentRepository
                .findById(tournamentId)
                .map(tournament -> {
                    validateByPredicate(tournament, Tournament::isFinished, TOURNAMENT_IS_FINISHED);
                    validateByPredicate(participantId, not(tournament.getParticipants()::contains), NOT_PARTICIPANT);

                    tournament.getParticipants().remove(participantId);

                    return tournamentRepository.save(tournament);
                })
                .map(this::map)
                .orElseThrow(() -> new ApiException(INVALID_TOURNAMENT_ID));
    }

    @Override
    @Transactional
    public TournamentResponse start(UUID tournamentId) {
        return tournamentRepository
                .findById(tournamentId)
                .map(tournament -> {
                    validateByPredicate(tournament, Tournament::isFinished, TOURNAMENT_IS_FINISHED);
                    validateByPredicate(tournament.getParticipants(), CollectionUtils::isEmpty, PARTICIPANTS_REQUIRED);

                    List<UUID> currentStageParticipants = new ArrayList<>(tournament.getParticipants());
                    List<UUID> winners = new ArrayList<>();

                    Map<Integer, Set<Match>> matchesMap = matchService.getMatches(tournamentId);

                    seq(matchesMap.keySet())
                            .sorted()
                            .reverse()
                            .forEach(stage -> {
                                shuffle(currentStageParticipants);

                                Set<Match> matches = matchesMap.get(stage);

                                if (currentStageParticipants.size() < matches.size() * 2) {
                                    int fillersCount = matches.size() * 2 - currentStageParticipants.size();
                                    range(0, fillersCount).forEach(x -> currentStageParticipants.add(null));
                                }

                                matches
                                        .forEach(match -> {
                                            UUID firstParticipant = currentStageParticipants.get(0);
                                            UUID secondParticipant = currentStageParticipants.get(1);

                                            matchService.play(match, firstParticipant, secondParticipant);
                                            currentStageParticipants.remove(firstParticipant);
                                            currentStageParticipants.remove(secondParticipant);

                                            winners.add(match.getWinner());
                                        });

                                currentStageParticipants.clear();
                                currentStageParticipants.addAll(winners);
                                winners.clear();
                            });

                    tournament.setWinner(currentStageParticipants.get(0));
                    tournament.setFinished(true);

                    return tournamentRepository.save(tournament);
                })
                .map(this::map)
                .orElseThrow(() -> new ApiException(INVALID_TOURNAMENT_ID));
    }

    private TournamentResponse map(Tournament tournament) {
        TournamentResponse map = modelMapper.map(tournament, TournamentResponse.class);
        map.setParticipants(participantService.get(tournament.getParticipants()));
        map.setStages(
                seq(matchService.getMatchIds(tournament.getId()))
                        .map(x -> new TournamentResponse.Stage(x.v1, getStageName(x.v1), x.v2))
                        .sorted(TournamentResponse.Stage::getStageNumber)
                        .toList());
        return map;
    }
}