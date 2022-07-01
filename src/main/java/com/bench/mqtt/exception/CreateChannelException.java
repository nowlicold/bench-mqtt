package com.bench.mqtt.exception;

public class CreateChannelException extends ConnectionException{
    public CreateChannelException(String reason) {
        super(reason);
    }
}
