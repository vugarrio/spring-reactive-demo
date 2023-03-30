package com.example.reactive.annotation.geotracking.exception;

public class GeoPointServiceException extends Exception {

    public GeoPointServiceException() { super(); }

    public GeoPointServiceException(String message) { super(message); }

    public GeoPointServiceException(String message, Throwable cause) { super(message, cause); }

    public GeoPointServiceException(Throwable cause) { super(cause); }

    public GeoPointServiceException(String message, Throwable cause, boolean enableSuppression,
                                    boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
