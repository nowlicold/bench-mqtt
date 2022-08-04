package com.bench.mqtt.client;

import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * <p>
 * ConnectClient 适配器接口用于统一连接
 * </p>
 *
 * @author Karl
 * @date 2022/8/3 11:34
 */
public interface ConnectClient {
    void connect() throws MqttException;
}
