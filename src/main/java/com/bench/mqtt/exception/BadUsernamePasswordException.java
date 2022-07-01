package com.bench.mqtt.exception;

public class BadUsernamePasswordException extends ConnectionException{
    public BadUsernamePasswordException(String reason) {
        super(reason);
    }
}
