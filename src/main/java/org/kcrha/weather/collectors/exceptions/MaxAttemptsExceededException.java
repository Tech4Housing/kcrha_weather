package org.kcrha.weather.collectors.exceptions;

public class MaxAttemptsExceededException extends RuntimeException {

    public MaxAttemptsExceededException(String message) {
        super(message);
    }
}
