package com.bench.mqtt.client.adapter;

import com.bench.mqtt.client.ConnectClient;
import com.bench.mqtt.client.MQTTClient;
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
@ConditionalOnProperty(name = "mqtt.client", matchIfMissing = true, havingValue = ClientTypeEnum.DEFAULT_NAME)
public class DefaultConnectClientAdapter implements ConnectClientAdapter {
    private final MQTTClient mqttClient;

    @Autowired
    public DefaultConnectClientAdapter(MQTTClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    @Override
    public boolean support(ClientTypeEnum clientType) {
        return clientType == ClientTypeEnum.DEFAULT;
    }

    @Override
    public ConnectClient get() {
        return mqttClient::connect;
    }
}
