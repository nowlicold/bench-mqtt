package com.bench.mqtt.callback;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * <p>
 * 发送 MQTT 消息成功回调接口
 * </p>
 *
 * @author Karl
 * @date 2022/7/1 11:44
 */
public interface DeliveryCompleteCallback {
    void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken);
}
