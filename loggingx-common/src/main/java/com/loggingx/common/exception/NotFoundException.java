package com.loggingx.common.exception;

public final class NotFoundException extends ApplicationException {

    public NotFoundException(String message) {
        super(ErrorCode.NOT_FOUND, message);
    }
}
