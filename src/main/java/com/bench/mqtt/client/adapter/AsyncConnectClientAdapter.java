package com.bench.mqtt.client.adapter;

import com.bench.mqtt.client.*;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * AsyncConnectClientHandler 适配器
 * </p>
 *
 * @author Karl
 * @date 2022/8/3 11:38
 */
@Component
public class AsyncConnectClientAdapter implements ConnectClientAdapter {
    private final MQTTAsyncClient mqttClient;

    @Autowired
    public AsyncConnectClientAdapter(MQTTAsyncClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    @Override
    public boolean support(ClientTypeEnum clientType) {
        return clientType == ClientTypeEnum.ASYNC;
    }

    @Override
    public ConnectClient get() {
        return mqttClient::connect;
    }
}
