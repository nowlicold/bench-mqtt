package com.bench.mqtt.reconnect.impl;

import com.bench.mqtt.client.MQTTClient;
import com.bench.mqtt.reconnect.Reconnector;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

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
public class DefaultReconnector implements Reconnector {

    @Override
    public void reconnect(MQTTClient mqttClient) throws MqttException {
        ReconnectTimer reconnectTimer = new ReconnectTimer();
        reconnectTimer.start(mqttClient);
    }

    static class ReconnectTimer {
        private final Timer timer;

        /*@Value("${mqtt.retry.count:3}")
        private Integer retryCount;*/

        @Value("${mqtt.retry.interval:1000}")
        private Integer retryInterval=1000;

        public ReconnectTimer() {
            timer = new Timer();
            //this.count = 0;
        }

        public void start(MQTTClient mqttClient) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        mqttClient.reconnect();
                        // 重试成功
                        log.info("reconnected");
                    } catch (Exception e) {
                        log.error("failed to reconnect. try to connect again.", e);
                        try {
                            mqttClient.connect();
                        } catch (MqttException ex) {
                            ex.printStackTrace();
                        }
                    }
                    timer.cancel();
                }
            }, retryInterval);
        }
    }
}
