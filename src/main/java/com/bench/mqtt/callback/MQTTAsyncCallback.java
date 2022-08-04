package com.bench.mqtt.callback;

import com.bench.mqtt.client.MQTTAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * <p>
 * 异步 MQTT 客户端回调接口聚合
 * </p>
 *
 * @author Karl
 * @date 2022/7/1 17:36
 */
public interface MQTTAsyncCallback {
    void connectComplete(boolean reconnect, String serverURI);

    void connectionLost(MQTTAsyncClient mqttClient, Throwable throwable) throws MqttException;

    void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken);

    void messageArrived(String topic, MqttMessage mqttMessage);
}
