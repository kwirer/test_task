package com.example.test;

import com.example.test.exception.ApiException;
import com.example.test.protocol.request.ParticipantAddRequest;
import com.example.test.protocol.request.TournamentAddRequest;
import com.example.test.protocol.response.ParticipantResponse;
import com.example.test.protocol.response.TournamentResponse;
import com.example.test.repository.MatchRepository;
import com.example.test.repository.ParticipantRepository;
import com.example.test.repository.TournamentRepository;
import com.example.test.service.MatchService;
import com.example.test.service.ParticipantService;
import com.example.test.service.TournamentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;
import java.util.UUID;

import static com.example.test.protocol.IErrorCode.INVALID_PARTICIPANT_ID;
import static com.example.test.utils.CommonUtils.getStageName;
import static com.example.test.utils.CommonUtils.isPowerOfTwo;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TestApplicationTests {

    @Autowired
    private MatchService matchService;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private TournamentRepository tournamentRepository;


    @Test
    public void isPowerOfTwoTest() {
        assertTrue(isPowerOfTwo(2));
        assertTrue(isPowerOfTwo(4));
        assertTrue(isPowerOfTwo(8));
        assertTrue(isPowerOfTwo(16));

        assertFalse(isPowerOfTwo(3));
        assertFalse(isPowerOfTwo(7));
        assertFalse(isPowerOfTwo(15));
    }

    @Test
    public void getStageNameTest() {
        assertEquals(getStageName(0), "Final");
        assertEquals(getStageName(1), "Semi-final");
        assertEquals(getStageName(6), "1/64 final");
    }

    @Test
    public void test() {
        ParticipantResponse response = participantService.add(new ParticipantAddRequest("1"));

        assertEquals(participantService.get(Set.of(response.getId())), Set.of(response));

        ApiException apiException = assertThrows(ApiException.class, () -> participantService.checkParticipants(Set.of(UUID.randomUUID())));
        assertEquals(apiException.getErrorCode(), INVALID_PARTICIPANT_ID);

        TournamentResponse tournament = tournamentService.add(new TournamentAddRequest(
                4,
                Set.of(
                        response.getId(),
                        participantService.add(new ParticipantAddRequest("2")).getId(),
                        participantService.add(new ParticipantAddRequest("3")).getId(),
                        participantService.add(new ParticipantAddRequest("4")).getId())));

        assertNotNull(tournamentService.start(tournament.getId()).getWinner());
    }
}
