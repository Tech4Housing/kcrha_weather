package org.kcrha.weather.models.cli;

public class MissingFileException extends RuntimeException {

    public MissingFileException(String message) {
        super(message);
    }
}
