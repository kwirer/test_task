package com.example.test.exception;

import com.example.test.protocol.ErrorCode;

public class ApiException extends RuntimeException {
    private static final long serialVersionUID = -8719780424208224259L;

    private final ErrorCode errorCode;

    public ApiException(ErrorCode errorCode) {
        super(errorCode.stringCode());

        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
