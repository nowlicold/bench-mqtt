package com.bench.mqtt.connect.impl;

import com.bench.mqtt.connect.ConnectClient;
import com.bench.mqtt.connect.Reconnector;
import com.bench.mqtt.exception.BadUsernamePasswordException;
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
    public void reconnect(ConnectClient connectClient) throws MqttException {
        ReconnectTimer reconnectTimer = new ReconnectTimer();
        reconnectTimer.start(connectClient);
    }

    private static class ReconnectTimer {
        private final Timer timer;

        @Value("${mqtt.retry.count:3}")
        private Integer retryCount = 3;

        @Value("${mqtt.retry.interval:3000}")
        private Integer retryInterval = 3000;

        // 重试次数
        private int currentRetry = 0;

        public ReconnectTimer() {
            timer = new Timer();
        }

        public void start(ConnectClient connectClient) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    log.info("run reconnection timer");
                    if (connectClient.isConnected()) {
                        log.info("Canceled timer for client is connected.");
                        stop();
                        return;
                    }

                    log.info("start to reconnect {} times. clientId: {}", ++currentRetry, connectClient.getClientId());
                    if (currentRetry > retryCount) {
                        connect();
                        return;
                    }

                    try {
                        connectClient.reconnect();
                    } catch (MqttException e) {
                        // 已经连接上了，忽略
                        if (e.getReasonCode() == MqttException.REASON_CODE_CONNECT_IN_PROGRESS) {
                            if (connectClient.isConnected()) {
                                stop();
                            }
                            return;
                        }
                        log.error("failed to reconnect. try to connect again.", e);
                        connect();
                    }
                    log.info("time end for this time");
                }

                private void connect() {
                    try {
                        connectClient.connect();
                    } catch (MqttException ex) {
                        log.error("failed to connect mqtt. ", ex);
                    }
                }

                private void stop() {
                    currentRetry = 0;
                    timer.cancel();
                }
            }, 50, retryInterval);
        }
    }
}
