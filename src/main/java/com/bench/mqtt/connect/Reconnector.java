package com.bench.mqtt.connect;

import com.bench.mqtt.client.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * <p>
 * 重新连接
 * </p>
 *
 * @author Karl
 * @date 2022/6/30 18:35
 */
public interface Reconnector {
    void reconnect(MqttClient mqttClient) throws MqttException;
}
