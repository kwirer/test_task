package com.example.test.configuration;

import com.example.test.exception.ApiException;
import com.example.test.protocol.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Order
@RestControllerAdvice(annotations = {RestController.class})
public class GlobalRestControllerAdvice {

    @ExceptionHandler(value = {ApiException.class})
    public ResponseEntity<FailedResponse> clientException(ApiException ex) {
        return ResponseEntity
                .status(BAD_REQUEST.value())
                .body(new FailedResponse(ex.getErrorCode()));
    }

    @Getter
    @ToString
    @EqualsAndHashCode
    private static class FailedResponse {
        @JsonInclude(NON_NULL)
        private final String message;

        public FailedResponse(ErrorCode errorCode) {
            this.message = errorCode.stringCode();
        }
    }
}
