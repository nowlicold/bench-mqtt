package com.bench.mqtt.connect.adapter;

import com.bench.mqtt.client.MqttClient;
import com.bench.mqtt.client.MqttClientType;
import com.bench.mqtt.connect.ConnectClient;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * <p>
 * DefaultConnectClientHandler 适配器
 * </p>
 *
 * @author Karl
 * @date 2022/8/3 11:38
 */
@Component
@ConditionalOnProperty(name = "mqtt.client", matchIfMissing = true, havingValue = MqttClientType.DEFAULT)
public class DefaultConnectClientAdapter implements ConnectClientAdapter {
    private final IMqttClient mqttClient;

    @Autowired
    public DefaultConnectClientAdapter(IMqttClient mqttClient) {
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
                if (!mqttClient.isConnected()){
                    mqttClient.reconnect();
                }

            }
            @Override
            public boolean isConnected() {
                return mqttClient.isConnected();
            }
            @Override
            public String getClientId() {
                return mqttClient.getClientId();
            }
        };
    }
}
