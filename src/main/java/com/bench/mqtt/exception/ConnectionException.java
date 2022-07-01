package com.bench.mqtt.exception;

public class ConnectionException extends Exception{
    public ConnectionException(String reason){
        super(reason);
    }
}
