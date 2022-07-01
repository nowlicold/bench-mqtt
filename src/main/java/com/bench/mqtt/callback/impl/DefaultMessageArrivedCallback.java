package com.bench.mqtt.callback.impl;

import com.bench.mqtt.callback.MessageArrivedCallback;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 接收到消息，默认回调
 * </p>
 *
 * @author Karl
 * @date 2022/7/1 11:30
 */
@Component
@Slf4j
public class DefaultMessageArrivedCallback implements MessageArrivedCallback {
    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) {
        log.info("MQTT message arrived. {}: {}", topic, new String(mqttMessage.getPayload()));
    }
}
