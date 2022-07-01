package com.bench.mqtt.client;

import com.bench.mqtt.callback.MQTTCallback;
import com.bench.mqtt.config.MQTTConfig;
import com.bench.mqtt.config.generator.MQTTConfigGenerator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * <p>
 * 同步 MQTT 客户端封装
 *
 * </p>
 *
 * @author Karl
 * @date 2022/7/1 18:53
 */
@Slf4j
@Component
public class MQTTClient implements IMqttClient {
    private final MQTTCallback mqttCallback;
    private final MQTTConfigGenerator mqttConfigGenerator;
    private IMqttClient mqttClient;

    @Autowired
    public MQTTClient(MQTTCallback mqttCallback, MQTTConfigGenerator mqttConfigGenerator) {
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
        private final MQTTCallback mqttCallback;

        public InternalCallback(MQTTCallback mqttCallback) {
            this.mqttCallback = mqttCallback;
        }

        @Override
        @SneakyThrows
        public void connectionLost(Throwable throwable) {
            mqttCallback.connectionLost(MQTTClient.this, throwable);
        }

        @Override
        public void connectComplete(boolean reconnect, String serverURI) {
            mqttCallback.connectComplete(reconnect, serverURI);
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

        MQTTConfig mqttConfig = mqttConfigGenerator.generator();

        mqttClient = new MqttClient(mqttConfig.getUrl(), mqttConfig.getClientId());
        mqttClient.setCallback(new InternalCallback(mqttCallback)); // 设置默认回调

        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(mqttConfig.getUsername());
        options.setPassword(mqttConfig.getPassword().toCharArray());
        options.setKeepAliveInterval(mqttConfig.getKeepAliveInterval());
        options.setConnectionTimeout(mqttConfig.getConnectionTimeout());
        options.setCleanSession(mqttConfig.isCleanSession());
        options.setAutomaticReconnect(true);

        log.info("connecting to MQTT Server. clientId: {}", mqttConfig.getClientId());
        this.mqttClient.connect(options);
    }

    @Override
    public void connect(MqttConnectOptions mqttConnectOptions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public IMqttToken connectWithResult(MqttConnectOptions mqttConnectOptions) throws MqttException {
        return mqttClient.connectWithResult(mqttConnectOptions);
    }

    @Override
    public void disconnect() throws MqttException {
        mqttClient.disconnect();
    }

    @Override
    public void disconnect(long l) throws MqttException {
        mqttClient.disconnect(l);
    }

    @Override
    public void disconnectForcibly() throws MqttException {
        mqttClient.disconnectForcibly();
    }

    @Override
    public void disconnectForcibly(long l) throws MqttException {
        mqttClient.disconnectForcibly(l);
    }

    @Override
    public void disconnectForcibly(long l, long l1) throws MqttException {
        mqttClient.disconnectForcibly(l, l1);
    }

    @Override
    public void subscribe(String s) throws MqttException {
        mqttClient.subscribe(s);
    }

    @Override
    public void subscribe(String[] strings) throws MqttException {
        mqttClient.subscribe(strings);
    }

    @Override
    public void subscribe(String s, int i) throws MqttException {
        mqttClient.subscribe(s, i);
    }

    @Override
    public void subscribe(String[] strings, int[] ints) throws MqttException {
        mqttClient.subscribe(strings, ints);
    }

    @Override
    public void subscribe(String s, IMqttMessageListener iMqttMessageListener) throws MqttException {
        mqttClient.subscribe(s, iMqttMessageListener);
    }

    @Override
    public void subscribe(String[] strings, IMqttMessageListener[] iMqttMessageListeners) throws MqttException {
        mqttClient.subscribe(strings, iMqttMessageListeners);
    }

    @Override
    public void subscribe(String s, int i, IMqttMessageListener iMqttMessageListener) throws MqttException {
        mqttClient.subscribe(s, i, iMqttMessageListener);
    }

    @Override
    public void subscribe(String[] strings, int[] ints, IMqttMessageListener[] iMqttMessageListeners) throws MqttException {
        mqttClient.subscribe(strings, ints, iMqttMessageListeners);
    }

    @Override
    public IMqttToken subscribeWithResponse(String s) throws MqttException {
        return mqttClient.subscribeWithResponse(s);
    }

    @Override
    public IMqttToken subscribeWithResponse(String s, IMqttMessageListener iMqttMessageListener) throws MqttException {
        return mqttClient.subscribeWithResponse(s, iMqttMessageListener);
    }

    @Override
    public IMqttToken subscribeWithResponse(String s, int i) throws MqttException {
        return mqttClient.subscribeWithResponse(s, i);
    }

    @Override
    public IMqttToken subscribeWithResponse(String s, int i, IMqttMessageListener iMqttMessageListener) throws MqttException {
        return mqttClient.subscribeWithResponse(s, i, iMqttMessageListener);
    }

    @Override
    public IMqttToken subscribeWithResponse(String[] strings) throws MqttException {
        return mqttClient.subscribeWithResponse(strings);
    }

    @Override
    public IMqttToken subscribeWithResponse(String[] strings, IMqttMessageListener[] iMqttMessageListeners) throws MqttException {
        return mqttClient.subscribeWithResponse(strings, iMqttMessageListeners);
    }

    @Override
    public IMqttToken subscribeWithResponse(String[] strings, int[] ints) throws MqttException {
        return mqttClient.subscribeWithResponse(strings, ints);
    }

    @Override
    public IMqttToken subscribeWithResponse(String[] strings, int[] ints, IMqttMessageListener[] iMqttMessageListeners) throws MqttException {
        return mqttClient.subscribeWithResponse(strings, ints, iMqttMessageListeners);
    }

    @Override
    public void unsubscribe(String s) throws MqttException {
        mqttClient.unsubscribe(s);
    }

    @Override
    public void unsubscribe(String[] strings) throws MqttException {
        mqttClient.unsubscribe(strings);
    }

    @Override
    public void publish(String s, byte[] bytes, int i, boolean b) throws MqttException {
        mqttClient.publish(s, bytes, i, b);
    }

    @Override
    public void publish(String s, MqttMessage mqttMessage) throws MqttException {
        mqttClient.publish(s, mqttMessage);
    }

    @Override
    public void setCallback(MqttCallback mqttCallback) {
        mqttClient.setCallback(mqttCallback);
    }

    @Override
    public MqttTopic getTopic(String s) {
        return mqttClient.getTopic(s);
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
    public String getServerURI() {
        return mqttClient.getServerURI();
    }

    @Override
    public IMqttDeliveryToken[] getPendingDeliveryTokens() {
        return mqttClient.getPendingDeliveryTokens();
    }

    @Override
    public void setManualAcks(boolean b) {
        mqttClient.setManualAcks(b);
    }

    @Override
    public void reconnect() throws MqttException {
        mqttClient.reconnect();
    }

    @Override
    public void messageArrivedComplete(int i, int i1) throws MqttException {
        mqttClient.messageArrivedComplete(i, i1);
    }

    @Override
    public void close() throws MqttException {
        mqttClient.close();
    }
}
