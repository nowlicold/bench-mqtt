package com.bench.mqtt.callback;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * <p>
 * 同步 MQTT 客户端回调接口聚合
 * </p>
 *
 * @author Karl
 * @date 2022/7/1 17:36
 */
public interface MqttCallback {
    void connectComplete(IMqttClient mqttClient, boolean reconnect, String serverURI);

    void connectionLost(IMqttClient mqttClient, Throwable throwable) throws MqttException;

    void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken);

    void messageArrived(String topic, MqttMessage mqttMessage);
}
