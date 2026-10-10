package com.loggingx.common.exception;

public class ApplicationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ErrorCode code;

    public ApplicationException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }

    public ErrorCode getCode() {
        return code;
    }
}
