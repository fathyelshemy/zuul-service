package com.zuul.limiter.demo.exceptions;

public enum ErrorInfo {

    INTERNAL_ERROR("1", "Internal error"),
    TOO_MANY_REQUESTS("53", "Too many requests");

    public final String code;
    public final String message;

    ErrorInfo(final String code, final String message) {
        this.code = code;
        this.message = message;
    }
}
