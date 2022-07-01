package com.bench.mqtt.callback.impl;

import com.bench.lang.base.string.utils.StringUtils;
import com.bench.mqtt.callback.DeliveryCompleteCallback;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 消息发送成功，默认处理
 * </p>
 *
 * @author Karl
 * @date 2022/7/1 11:54
 */
@Component
@Slf4j
public class DefaultDeliveryCompleteCallback implements DeliveryCompleteCallback {
    @SneakyThrows
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        String[] topics = iMqttDeliveryToken.getTopics();
        String payload = new String(iMqttDeliveryToken.getMessage().getPayload());
        log.info("MQTT message delivered. {} -> {}", payload, StringUtils.join(topics, StringUtils.COMMA_SIGN));
    }
}
