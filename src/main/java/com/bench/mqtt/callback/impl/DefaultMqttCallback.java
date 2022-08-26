package com.bench.mqtt.callback.impl;

import com.bench.lang.base.string.utils.StringUtils;
import com.bench.mqtt.callback.MqttCallback;
import com.bench.mqtt.connect.Reconnector;
import com.bench.mqtt.connect.adapter.DefaultConnectClientAdapter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 同步 MQTT 客户端，全局回调器
 *
 * @author Karl
 * @date 2022/2/18
 */
@Component
@Slf4j
public class DefaultMqttCallback implements MqttCallback {
    private final Reconnector reconnector;

    @Autowired
    public DefaultMqttCallback(Reconnector reconnector) {
        this.reconnector = reconnector;
    }

    /**
     * 连接断开时的回调
     * <p>
     * 执行重连
     *
     * @param throwable throwable
     */
    @Override
    public void connectionLost(IMqttClient mqttClient, Throwable throwable) throws MqttException {
        log.error("MQTT disconnected: {}", throwable.getMessage());
        throwable.printStackTrace();
        log.info("MQTT reconnecting...");
        //重新连接
        reconnector.reconnect(new DefaultConnectClientAdapter(mqttClient).getAdapter());
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
    public void connectComplete(IMqttClient mqttClient, boolean reconnect, String serverURI) {
        log.info("MQTT connected");

        try {
            mqttClient.publish("/felo/app/yd996720A2600649649D836C46E41FBB0A/9a7ab66b87c470a57d2ffa7047494f9e", "test".getBytes(StandardCharsets.UTF_8), 1, false);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }

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
        log.info("MQTT message arrived. {}: {}", topic, new String(mqttMessage.getPayload()));
    }

    /**
     * 消息发送成功时的回调
     *
     * @param iMqttDeliveryToken iMqttDeliveryToken
     */
    @SneakyThrows
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        String[] topics = iMqttDeliveryToken.getTopics();
        MqttMessage message = iMqttDeliveryToken.getMessage();
        if (Objects.nonNull(message)) {
            String payload = new String(message.getPayload());
            log.info("MQTT message delivered. {} -> {}", payload, StringUtils.join(topics, StringUtils.COMMA_SIGN));
        }
    }
}
