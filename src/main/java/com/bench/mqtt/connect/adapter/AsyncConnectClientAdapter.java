package com.bench.mqtt.connect.adapter;

import com.bench.mqtt.client.*;
import com.bench.mqtt.connect.ConnectClient;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@ConditionalOnProperty(name = "mqtt.client", havingValue = MqttClientType.ASYNC)
public class AsyncConnectClientAdapter implements ConnectClientAdapter {
    private final IMqttAsyncClient mqttClient;

    @Autowired
    public AsyncConnectClientAdapter(IMqttAsyncClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    @Override
    public ConnectClient getAdapter() {
        return new ConnectClient() {
            @Override
            public void connect() throws MqttException {
                mqttClient.connect();
            }

            @Override
            public void reconnect() throws MqttException {
                mqttClient.reconnect();
            }
        };
    }
}
