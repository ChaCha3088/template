package com.ssong.exception.auth.jwt;

public class AccessTokenException extends RuntimeException {
    public AccessTokenException(String message) {
        super(message);
    }
}
