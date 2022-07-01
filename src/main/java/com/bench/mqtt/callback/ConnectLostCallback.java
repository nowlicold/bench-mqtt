package com.bench.mqtt.callback;

import com.bench.mqtt.client.MQTTClient;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * <p>
 * MQTT 连接断开回调接口
 * </p>
 *
 * @author Karl
 * @date 2022/7/1 11:22
 */
public interface ConnectLostCallback {
    void connectionLost(MQTTClient mqttClient, Throwable throwable) throws MqttException;
}
