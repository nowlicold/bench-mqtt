package com.bench.mqtt.connect.impl;

import com.bench.mqtt.client.MqttClient;
import com.bench.mqtt.connect.ConnectClient;
import com.bench.mqtt.connect.Reconnector;
import com.bench.mqtt.connect.adapter.ConnectClientAdapter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantLock;

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

    static class ReconnectTimer {
        private final Timer timer;

        /*@Value("${mqtt.retry.count:3}")
        private Integer retryCount;*/

        @Value("${mqtt.retry.interval:1000}")
        private Integer retryInterval = 1000;

        public ReconnectTimer() {
            timer = new Timer();
            //this.count = 0;
        }

        public void start(ConnectClient connectClient) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    log.info("run reconnection timer");

                    if (connectClient.isConnected()) {
                        log.info("Canceled timer for client is connected.");
                        timer.cancel();
                        return;
                    }

                    log.info("start to reconnect");

                    try {
                        connectClient.reconnect();
                    } catch (MqttException e) {
                        // 已经连接上了，忽略
                        if (e.getReasonCode() == MqttException.REASON_CODE_CONNECT_IN_PROGRESS) {
                            timer.cancel();
                            return;
                        }

                        log.error("failed to reconnect. try to connect again.", e);
                        try {
                            connectClient.connect();
                        } catch (MqttException ex) {
                            log.error("failed to connect mqtt. ", ex);
                        }
                    }
                }
            }, 0, retryInterval);
        }
    }

   /* public static void main(String[] args) {
        ReconnectTimer reconnectTimer = new ReconnectTimer();
        reconnectTimer.start(null);
    }*/
}
