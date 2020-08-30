package com.example.test.controller;

import com.example.test.protocol.request.TournamentAddRequest;
import com.example.test.protocol.response.TournamentResponse;
import com.example.test.service.TournamentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@Api(tags = "Tournament API")
@RequestMapping(produces = APPLICATION_JSON_VALUE, value = "/tournament")
public class TournamentController {

    private final TournamentService tournamentService;

    @PostMapping
    @ApiOperation("Add tournament")
    public ResponseEntity<TournamentResponse> add(@RequestBody TournamentAddRequest request) {
        return ok(tournamentService.add(request));
    }

    @GetMapping("/{tournamentId}")
    @ApiOperation("Get tournament")
    public ResponseEntity<TournamentResponse> add(@PathVariable(value = "tournamentId") UUID tournamentId) {
        return ok(tournamentService.get(tournamentId));
    }

    @GetMapping("/start/{tournamentId}")
    @ApiOperation("Start tournament")
    public ResponseEntity<TournamentResponse> start(@PathVariable(value = "tournamentId") UUID tournamentId) {
        return ok(tournamentService.start(tournamentId));
    }

    @GetMapping("/add/{tournamentId}/{participantId}")
    @ApiOperation("Add tournament participant")
    public ResponseEntity<TournamentResponse> addParticipant(@PathVariable(value = "tournamentId") UUID tournamentId,
                                                             @PathVariable(value = "participantId") UUID participantId) {
        return ok(tournamentService.addParticipant(tournamentId, participantId));
    }

    @GetMapping("/delete/{tournamentId}/{participantId}")
    @ApiOperation("Delete tournament participant")
    public ResponseEntity<TournamentResponse> deleteParticipant(@PathVariable(value = "tournamentId") UUID tournamentId,
                                                                @PathVariable(value = "participantId") UUID participantId) {
        return ok(tournamentService.deleteParticipant(tournamentId, participantId));
    }
}