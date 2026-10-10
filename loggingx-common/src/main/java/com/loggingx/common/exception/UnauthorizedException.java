package com.loggingx.common.exception;

public final class UnauthorizedException extends ApplicationException {

    public UnauthorizedException(String message) {
        super(ErrorCode.UNAUTHORIZED, message);
    }
}
