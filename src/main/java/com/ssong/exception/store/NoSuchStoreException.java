package com.ssong.exception.store;

public class NoSuchStoreException extends RuntimeException {
    public NoSuchStoreException(String message) {
        super(message);
    }
}
