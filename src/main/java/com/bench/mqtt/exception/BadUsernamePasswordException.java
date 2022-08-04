package com.bench.mqtt.exception;

import org.eclipse.paho.client.mqttv3.MqttException;

public class BadUsernamePasswordException extends MqttException {
    public BadUsernamePasswordException(int reasonCode) {
        super(reasonCode);
    }
}
