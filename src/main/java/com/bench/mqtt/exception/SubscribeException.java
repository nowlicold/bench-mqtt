package com.bench.mqtt.exception;

public class SubscribeException extends ConnectionException{
    public SubscribeException(String reason) {
        super(reason);
    }
}
