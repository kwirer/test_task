package com.example.test.protocol;

import org.springframework.http.HttpStatus;

/**
 * Common interface for all com.dah error codes
 */
public interface ErrorCode {
    HttpStatus status();

    String stringCode();
}
