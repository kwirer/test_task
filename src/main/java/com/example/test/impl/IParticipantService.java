package com.example.test.impl;

import com.example.test.entities.Participant;
import com.example.test.protocol.request.ParticipantAddRequest;
import com.example.test.protocol.response.ParticipantResponse;
import com.example.test.repository.ParticipantRepository;
import com.example.test.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.lambda.Seq;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static com.example.test.protocol.IErrorCode.*;
import static com.example.test.utils.CommonUtils.validateByPredicate;
import static java.lang.Boolean.FALSE;
import static org.jooq.lambda.Seq.seq;

@Slf4j
@Service
@RequiredArgsConstructor
public class IParticipantService implements ParticipantService {

    private final ModelMapper modelMapper;

    private final ParticipantRepository participantRepository;

    @Override
    public ParticipantResponse add(ParticipantAddRequest request) {
        validateByPredicate(request.getName(), Objects::isNull, NAME_REQUIRED);
        validateByPredicate(request.getName(), String::isBlank, NAME_REQUIRED);
        validateByPredicate(participantRepository.countByName(request.getName()), count -> count != 0, NAME_NOT_UNIQUE);

        return map(participantRepository.save(new Participant(request.getName())));
    }

    @Override
    public void checkParticipants(Set<UUID> participantIds) {
        validateByPredicate(
                participantIds.size() == participantRepository.countByIdIn(participantIds),
                FALSE::equals,
                INVALID_PARTICIPANT_ID);
    }

    @Override
    public Set<ParticipantResponse> get(Set<UUID> participantIds) {
        return seq(participantRepository.findAllById(participantIds))
                .map(this::map)
                .sorted(ParticipantResponse::getName)
                .toSet();
    }

    @Override
    public Map<UUID, ParticipantResponse> get(UUID... participantIds) {
        return seq(participantRepository.findAllById(Seq.of(participantIds).filter(Objects::nonNull).toSet()))
                .map(this::map)
                .toMap(ParticipantResponse::getId);
    }

    private ParticipantResponse map(Participant participant) {
        return modelMapper.map(participant, ParticipantResponse.class);
    }
}