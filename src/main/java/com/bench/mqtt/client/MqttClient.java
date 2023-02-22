package com.bench.mqtt.client;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * <p>
 *
 * </p>
 *
 * @author Karl
 * @date 2023/2/22 16:31
 */
public interface MqttClient extends AutoCloseable {
    void connect() throws MqttException;

    void subscribe(String topicFilter, int qos) throws MqttException;

    void subscribe(String topicFilter, int qos, IMqttMessageListener messageListener) throws MqttException;

    void publish(String topic, byte[] payload, int qos, boolean retained) throws MqttException;

    void reconnect() throws MqttException;

    boolean isConnected();

    String getClientId();

    void setCallback(org.eclipse.paho.client.mqttv3.MqttCallback mqttCallback) throws MqttException;

    void disconnect() throws MqttException;
}
