package com.loggingx.common.exception;

public final class ConflictException extends ApplicationException {

    public ConflictException(String message) {
        super(ErrorCode.CONFLICT, message);
    }
}
