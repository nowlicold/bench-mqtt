package com.bench.mqtt.reconnect;

import com.bench.mqtt.client.MQTTAsyncClient;
import com.bench.mqtt.client.MQTTClient;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * <p>
 * 重新连接
 * </p>
 *
 * @author Karl
 * @date 2022/6/30 18:35
 */
public interface AsyncReconnector {
    void reconnect(MQTTAsyncClient mqttClient) throws MqttException;
}
