package com.bench.mqtt.callback.impl;

import com.bench.mqtt.callback.ConnectLostCallback;
import com.bench.mqtt.reconnect.Reconnector;
import com.bench.mqtt.client.MQTTClient;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 同步 MQTT 客户端 连接断开，默认处理
 *
 * </p>
 *
 * @author Karl
 * @date 2022/6/30 18:35
 */
@Component
@Slf4j
public class DefaultConnectLostCallback implements ConnectLostCallback {
    private final Reconnector reconnector;

    @Autowired
    public DefaultConnectLostCallback(Reconnector reconnector) {
        this.reconnector = reconnector;
    }

    @Override
    public void connectionLost(MQTTClient mqttClient, Throwable throwable) throws MqttException {
        log.error("MQTT disconnected: {}", throwable.getMessage());
        throwable.printStackTrace();
        log.info("MQTT reconnecting...");
        //重新连接
        reconnector.reconnect(mqttClient);
    }
}
