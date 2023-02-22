package com.bench.mqtt.client.impl;

import com.bench.mqtt.callback.MqttCallback;
import com.bench.mqtt.client.MqttClient;
import com.bench.mqtt.client.MqttClientType;
import com.bench.mqtt.config.MqttConfig;
import com.bench.mqtt.config.generator.MqttConfigGenerator;
import com.bench.mqtt.exception.BadUsernamePasswordException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * <p>
 * 异步 MQTT 客户端封装
 *
 * </p>
 *
 * @author Karl
 * @date 2022/7/1 18:53
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "mqtt.client", havingValue = MqttClientType.ASYNC)
public class MqttAsyncClientImpl implements MqttClient {
    private final MqttCallback mqttCallback;
    private final MqttConfigGenerator mqttConfigGenerator;
    private IMqttAsyncClient mqttClient;

    @Autowired
    public MqttAsyncClientImpl(MqttCallback mqttCallback, MqttConfigGenerator mqttConfigGenerator) {
        this.mqttCallback = mqttCallback;
        this.mqttConfigGenerator = mqttConfigGenerator;
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
            mqttCallback.connectionLost(MqttAsyncClientImpl.this, throwable);
        }

        @Override
        public void connectComplete(boolean reconnect, String serverURI) {
            mqttCallback.connectComplete(MqttAsyncClientImpl.this, reconnect, serverURI);
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

    @Override
    public void connect() throws MqttException {
        if (!Objects.isNull(mqttClient) && mqttClient.isConnected()) {
            disconnect();
            close();
        }

        MqttConfig mqttConfig = mqttConfigGenerator.generator();

        mqttClient = new MqttAsyncClient(mqttConfig.getUrl(), mqttConfig.getClientId(), new MemoryPersistence());
        mqttClient.setCallback(new InternalCallback(mqttCallback)); // 设置默认回调

        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(mqttConfig.getUsername());
        options.setPassword(mqttConfig.getPassword().toCharArray());
        options.setKeepAliveInterval(mqttConfig.getKeepAliveInterval());
        options.setConnectionTimeout(mqttConfig.getConnectionTimeout());
        options.setCleanSession(mqttConfig.isCleanSession());
        options.setMaxInflight(mqttConfig.getMaxInflight());

        log.info("connecting to MQTT Server. clientId: {}", mqttConfig.getClientId());
        IMqttToken token = this.mqttClient.connect(options);
        checkToken(token);
    }

    private void checkToken(IMqttToken token) throws MqttException {
        token.waitForCompletion();
        if (token.getException() != null) {
            throw token.getException();
        }
    }

    @Override
    public void publish(String s, byte[] bytes, int i, boolean b) throws MqttException {
        checkConnect();
        IMqttDeliveryToken token = mqttClient.publish(s, bytes, i, b);
        checkToken(token);
    }

    @Override
    public void subscribe(String s, int i, IMqttMessageListener iMqttMessageListener) throws MqttException {
        IMqttToken token = mqttClient.subscribe(s, i, iMqttMessageListener);
        checkToken(token);
    }

    @Override
    public void subscribe(String topicFilter, int qos) throws MqttException {
        IMqttToken token = mqttClient.subscribe(topicFilter, qos);
        checkToken(token);
    }

    @Override
    public void setCallback(org.eclipse.paho.client.mqttv3.MqttCallback mqttCallback) {
        mqttClient.setCallback(mqttCallback);
    }

    @Override
    public void disconnect() throws MqttException {
        IMqttToken token = mqttClient.disconnect();
        checkToken(token);
    }

    @Override
    public void reconnect() throws MqttException {
        try {
            if (!mqttClient.isConnected()) mqttClient.reconnect();
        } catch (MqttException e) {
            if (e.getReasonCode() == MqttException.REASON_CODE_FAILED_AUTHENTICATION) {
                // 用户名或密码错误
                throw new BadUsernamePasswordException(e.getReasonCode());
            }
            throw e;
        }
    }

    public boolean isConnected() {
        return mqttClient.isConnected();
    }

    @Override
    public String getClientId() {
        return mqttClient.getClientId();
    }

    @Override
    public void close() throws MqttException {
        mqttClient.close();
    }

    /**
     * <p>
     * 检查连接状态，如果连接断开则尝试重连
     * </p>
     *
     * @author Karl
     * @date 2022/9/20 14:26
     */
    @SneakyThrows
    private void checkConnect() {
        if (!isConnected()) {
            mqttCallback.connectionLost(this, new Throwable("Try to reconnect for checking connect"));
        }
    }
}
