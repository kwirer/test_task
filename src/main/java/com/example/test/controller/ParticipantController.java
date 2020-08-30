package com.example.test.controller;

import com.example.test.protocol.request.ParticipantAddRequest;
import com.example.test.protocol.response.ParticipantResponse;
import com.example.test.service.ParticipantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@Api(tags = "Participant API")
@RequestMapping(produces = APPLICATION_JSON_VALUE, value = "/participant")
public class ParticipantController {

    private final ParticipantService participantService;

    @PostMapping
    @ApiOperation("Add participant")
    public ResponseEntity<ParticipantResponse> add(@RequestBody ParticipantAddRequest request) {
        return ok(participantService.add(request));
    }
}