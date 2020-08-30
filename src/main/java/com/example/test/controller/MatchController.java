package com.example.test.controller;

import com.example.test.protocol.response.MatchResponse;
import com.example.test.service.MatchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@Api(tags = "Match API")
@RequestMapping(produces = APPLICATION_JSON_VALUE, value = "/match")
public class MatchController {

    private final MatchService matchService;

    @GetMapping("/{matchId}")
    @ApiOperation("Get match")
    public ResponseEntity<MatchResponse> add(@PathVariable(value = "matchId") UUID matchId) {
        return ok(matchService.get(matchId));
    }
}