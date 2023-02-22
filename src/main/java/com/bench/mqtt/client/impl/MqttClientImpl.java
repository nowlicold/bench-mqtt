package com.bench.mqtt.client.impl;

import com.bench.mqtt.callback.MqttCallback;
import com.bench.mqtt.client.MqttClient;
import com.bench.mqtt.client.MqttClientType;
import com.bench.mqtt.config.MqttConfig;
import com.bench.mqtt.config.generator.MqttConfigGenerator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * MQTTClientImpl
 * </p>
 *
 * @author Karl
 * @date 2023/2/22 16:48
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "mqtt.client", matchIfMissing = true, havingValue = MqttClientType.DEFAULT)
public class MqttClientImpl implements MqttClient {
    private final MqttCallback mqttCallback;
    private final MqttConfigGenerator mqttConfigGenerator;
    private IMqttClient mqttClient;

    private final Lock lock = new ReentrantLock();

    @Autowired
    public MqttClientImpl(MqttCallback mqttCallback, MqttConfigGenerator mqttConfigGenerator) {
        this.mqttCallback = mqttCallback;
        this.mqttConfigGenerator = mqttConfigGenerator;
    }

    @Override
    public void connect() throws MqttException {
        if (!Objects.isNull(mqttClient) && mqttClient.isConnected()) {
            return;
        }

        if (lock.tryLock()) {
            try {
                if (!Objects.isNull(mqttClient) && mqttClient.isConnected()) {
                    return;
                }

                if (!Objects.isNull(mqttClient)) {
                    mqttClient.close();
                }

                MqttConfig mqttConfig = mqttConfigGenerator.generator();
                mqttClient = new org.eclipse.paho.client.mqttv3.MqttClient(mqttConfig.getUrl(), mqttConfig.getClientId(), new MemoryPersistence());
                mqttClient.setCallback(new InternalCallback(mqttCallback)); // 设置默认回调

                MqttConnectOptions options = new MqttConnectOptions();
                options.setUserName(mqttConfig.getUsername());
                options.setPassword(mqttConfig.getPassword().toCharArray());
                options.setKeepAliveInterval(mqttConfig.getKeepAliveInterval());
                options.setConnectionTimeout(mqttConfig.getConnectionTimeout());
                options.setCleanSession(mqttConfig.isCleanSession());
                options.setMaxInflight(mqttConfig.getMaxInflight());

                log.info("connecting to MQTT Server. clientId: {}", mqttConfig.getClientId());
                this.mqttClient.connect(options);
            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    public void subscribe(String topicFilter, int qos) throws MqttException {
        mqttClient.subscribe(topicFilter, qos);
    }

    @Override
    public void subscribe(String topicFilter, int qos, IMqttMessageListener messageListener) throws MqttException {
        mqttClient.subscribe(topicFilter, qos, messageListener);
    }

    @Override
    public void publish(String topic, byte[] payload, int qos, boolean retained) throws MqttException {
        mqttClient.publish(topic, payload, qos, retained);
    }

    @Override
    public void reconnect() throws MqttException {
        if (!mqttClient.isConnected()) mqttClient.reconnect();
    }

    @Override
    public boolean isConnected() {
        return mqttClient.isConnected();
    }

    @Override
    public String getClientId() {
        return mqttClient.getClientId();
    }

    @Override
    public void setCallback(org.eclipse.paho.client.mqttv3.MqttCallback mqttCallback) {
        mqttClient.setCallback(mqttCallback);
    }

    @Override
    public void disconnect() throws MqttException {
        mqttClient.disconnect();
    }

    @Override
    public void close() throws Exception {
        mqttClient.close();
    }

    /**
     * <p>
     * 全局回调
     * 用于将 MqttCallbackExtended 转换成 MQTTCallback
     * 并将 client 对象传递出去，避免循环依赖
     * </p>
     *
     * @author Karl
     * @date 2022/7/1 17:39
     */
    class InternalCallback implements MqttCallbackExtended {
        private final MqttCallback mqttCallback;

        public InternalCallback(MqttCallback mqttCallback) {
            this.mqttCallback = mqttCallback;
        }

        @Override
        @SneakyThrows
        public void connectionLost(Throwable throwable) {
            mqttCallback.connectionLost(MqttClientImpl.this, throwable);
        }

        @Override
        public void connectComplete(boolean reconnect, String serverURI) {
            mqttCallback.connectComplete(MqttClientImpl.this, reconnect, serverURI);
        }

        @Override
        public void messageArrived(String topic, MqttMessage mqttMessage) {
            mqttCallback.messageArrived(topic, mqttMessage);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
            mqttCallback.deliveryComplete(iMqttDeliveryToken);
        }
    }
}
