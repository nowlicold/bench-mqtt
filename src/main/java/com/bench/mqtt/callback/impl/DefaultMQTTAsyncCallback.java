package com.bench.mqtt.callback.impl;

import com.bench.mqtt.callback.*;
import com.bench.mqtt.client.MQTTAsyncClient;
import com.bench.mqtt.client.MQTTClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 异步 MQTT 客户端，全局回调器
 *
 * @author Karl
 * @date 2022/2/18
 */
@Component
@Slf4j
public class DefaultMQTTAsyncCallback implements MQTTAsyncCallback {
    private final ConnectCompleteCallback connectCompleteCallback;
    private final ConnectLostAsyncCallback connectLostAsyncCallback;
    private final DeliveryCompleteCallback deliveryCompleteCallback;
    private final MessageArrivedCallback messageArrivedCallback;

    @Autowired
    public DefaultMQTTAsyncCallback(ConnectCompleteCallback connectCompleteCallback, ConnectLostAsyncCallback connectLostAsyncCallback, DeliveryCompleteCallback deliveryCompleteCallback, MessageArrivedCallback messageArrivedCallback) {
        this.connectCompleteCallback = connectCompleteCallback;
        this.connectLostAsyncCallback = connectLostAsyncCallback;
        this.deliveryCompleteCallback = deliveryCompleteCallback;
        this.messageArrivedCallback = messageArrivedCallback;
    }

    /**
     * 连接断开时的回调
     * <p>
     * 执行重连
     *
     * @param throwable throwable
     */
    @SneakyThrows
    @Override
    public void connectionLost(MQTTAsyncClient mqttClient, Throwable throwable) {
        this.connectLostAsyncCallback.connectionLost(mqttClient, throwable);
    }

    /**
     * 连接/重连成功后调用
     * <p>
     * 如果设置了 clean session, 一般会重新订阅一些 topic
     *
     * @param reconnect reconnect
     * @param serverURI serverURI
     */
    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        this.connectCompleteCallback.connectComplete(reconnect, serverURI);
    }

    /**
     * 收到下推消息时的回调
     * 如果在订阅的时候设了监听器则事件不会到达
     *
     * @param topic       topic
     * @param mqttMessage message
     */
    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) {
        this.messageArrivedCallback.messageArrived(topic, mqttMessage);
    }

    /**
     * 消息发送成功时的回调
     *
     * @param iMqttDeliveryToken iMqttDeliveryToken
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        this.deliveryCompleteCallback.deliveryComplete(iMqttDeliveryToken);
    }
}
