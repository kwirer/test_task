package com.example.test.protocol;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public enum IErrorCode implements ErrorCode {
    NAME_REQUIRED,
    NAME_NOT_UNIQUE,

    INVALID_MATCH_ID,

    TOURNAMENT_IS_FINISHED,
    PARTICIPANTS_REQUIRED,
    INVALID_PARTICIPANT_ID,
    INVALID_MAX_PARTICIPANTS_COUNT,
    INVALID_PARTICIPANTS_COUNT,
    INVALID_TOURNAMENT_ID,
    ALREADY_PARTICIPANT,
    MAX_PARTICIPANTS_REACHED,
    NOT_PARTICIPANT;

    private final HttpStatus status = BAD_REQUEST;

    @Override
    public HttpStatus status() {
        return status;
    }

    @Override
    public String stringCode() {
        return name().toLowerCase();
    }
}
