package com.bench.mqtt.reconnect.impl;

import com.bench.mqtt.client.MQTTAsyncClient;
import com.bench.mqtt.reconnect.AsyncReconnector;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 重新连接
 * </p>
 *
 * @author Karl
 * @date 2022/6/30 18:35
 */
@Component
@Slf4j
public class AsyncDefaultReconnector implements AsyncReconnector {
    @Override
    public void reconnect(MQTTAsyncClient mqttClient) throws MqttException {
        mqttClient.reconnect();
        /*final int RETRY_COUNT = 3;
        final int RETRY_INTERVAL = 3000;
        for (int retryCount = 0; retryCount < RETRY_COUNT; retryCount++) {
            try {
                Thread.sleep(RETRY_INTERVAL);
                mqttClient.reconnect();
                // 重试成功
                log.info("reconnected");
                break;
            } catch (BadUsernamePasswordException e) {
                mqttClient.conn(e.getCause());
                return;
            } catch (Exception e) {
                // 最后一次重试不成功
                if (retryCount == RETRY_COUNT - 1) {
                    this.reconnectFailed(e.getCause());
                }
            }
        }*/
    }
}
