package com.example.test.protocol.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@Data
@NoArgsConstructor
@JsonInclude(NON_EMPTY)
public class ParticipantResponse {
    private UUID id;
    private String name;

    public ParticipantResponse(UUID id, String name) {
        this.id = id;
        this.name = name;
    }
}
