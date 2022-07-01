package com.bench.mqtt.callback;

import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * <p>
 * 接收到 MQTT 的消息回调接口
 * </p>
 *
 * @author Karl
 * @date 2022/7/1 11:44
 */
public interface MessageArrivedCallback {
    void messageArrived(String topic, MqttMessage mqttMessage);
}
