package com.microservices.twitter.exceptions;

public class TwitterServiceException extends RuntimeException {
    public TwitterServiceException() {
        super();
    }

    public TwitterServiceException(String message) {
        super(message);
    }

    public TwitterServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
