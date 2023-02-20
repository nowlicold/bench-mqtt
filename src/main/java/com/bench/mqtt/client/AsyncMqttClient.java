package com.bench.mqtt.client;

import com.bench.mqtt.callback.AsyncMqttCallback;
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
public class AsyncMqttClient implements IMqttAsyncClient {
    private final AsyncMqttCallback mqttCallback;
    private final MqttConfigGenerator mqttConfigGenerator;
    private IMqttAsyncClient mqttClient;

    @Autowired
    public AsyncMqttClient(AsyncMqttCallback mqttCallback, MqttConfigGenerator mqttConfigGenerator) {
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
        private final AsyncMqttCallback mqttCallback;

        public InternalCallback(AsyncMqttCallback mqttCallback) {
            this.mqttCallback = mqttCallback;
        }

        @Override
        @SneakyThrows
        public void connectionLost(Throwable throwable) {
            mqttCallback.connectionLost(AsyncMqttClient.this, throwable);
        }

        @Override
        public void connectComplete(boolean reconnect, String serverURI) {
            mqttCallback.connectComplete(AsyncMqttClient.this, reconnect, serverURI);
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
    public IMqttToken connect() throws MqttException {
        if (!Objects.isNull(mqttClient) && mqttClient.isConnected()) {
            disconnect();
            close();
        }

        MqttConfig mqttConfig = mqttConfigGenerator.generator();

        mqttClient = createMqttClient(mqttConfig.getUrl(), mqttConfig.getClientId());
        mqttClient.setCallback(new InternalCallback(mqttCallback)); // 设置默认回调

        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(mqttConfig.getUsername());
        options.setPassword(mqttConfig.getPassword().toCharArray());
        options.setKeepAliveInterval(mqttConfig.getKeepAliveInterval());
        options.setConnectionTimeout(mqttConfig.getConnectionTimeout());
        options.setCleanSession(mqttConfig.isCleanSession());

        log.info("connecting to MQTT Server. clientId: {}", mqttConfig.getClientId());
        IMqttToken token = this.mqttClient.connect(options);
        token.waitForCompletion();
        return token;
    }

    @Override
    public IMqttToken connect(MqttConnectOptions mqttConnectOptions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public IMqttToken connect(Object o, IMqttActionListener iMqttActionListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public IMqttToken connect(MqttConnectOptions mqttConnectOptions, Object o, IMqttActionListener iMqttActionListener) {
        throw new UnsupportedOperationException();
    }

    private IMqttAsyncClient createMqttClient(String url, String clientId) throws MqttException {
        return new org.eclipse.paho.client.mqttv3.MqttAsyncClient(url, clientId, new MemoryPersistence());
    }

    @Override
    public IMqttToken disconnect() throws MqttException {
        return mqttClient.disconnect();
    }

    @Override
    public IMqttToken disconnect(long l) throws MqttException {
        return mqttClient.disconnect(l);
    }

    @Override
    public IMqttToken disconnect(Object o, IMqttActionListener iMqttActionListener) throws MqttException {
        return mqttClient.disconnect(o, iMqttActionListener);
    }

    @Override
    public IMqttToken disconnect(long l, Object o, IMqttActionListener iMqttActionListener) throws MqttException {
        return mqttClient.disconnect(l, o, iMqttActionListener);
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
    public IMqttDeliveryToken publish(String s, byte[] bytes, int i, boolean b) throws MqttException {
        checkConnect();
        return mqttClient.publish(s, bytes, i, b);
    }

    @Override
    public IMqttDeliveryToken publish(String s, byte[] bytes, int i, boolean b, Object o, IMqttActionListener iMqttActionListener) throws MqttException {
        checkConnect();
        return mqttClient.publish(s, bytes, i, b, o, iMqttActionListener);
    }

    @Override
    public IMqttDeliveryToken publish(String s, MqttMessage mqttMessage) throws MqttException {
        checkConnect();
        return mqttClient.publish(s, mqttMessage);
    }

    @Override
    public IMqttDeliveryToken publish(String s, MqttMessage mqttMessage, Object o, IMqttActionListener iMqttActionListener) throws MqttException {
        checkConnect();
        return mqttClient.publish(s, mqttMessage, o, iMqttActionListener);
    }

    @Override
    public IMqttToken subscribe(String s, int i) throws MqttException {
        return mqttClient.subscribe(s, i);
    }

    @Override
    public IMqttToken subscribe(String s, int i, Object o, IMqttActionListener iMqttActionListener) throws MqttException {
        return mqttClient.subscribe(s, i, o, iMqttActionListener);
    }

    @Override
    public IMqttToken subscribe(String[] strings, int[] ints) throws MqttException {
        return mqttClient.subscribe(strings, ints);
    }

    @Override
    public IMqttToken subscribe(String[] strings, int[] ints, Object o, IMqttActionListener iMqttActionListener) throws MqttException {
        return mqttClient.subscribe(strings, ints, o, iMqttActionListener);
    }

    @Override
    public IMqttToken subscribe(String s, int i, Object o, IMqttActionListener iMqttActionListener, IMqttMessageListener iMqttMessageListener) throws MqttException {
        return mqttClient.subscribe(s, i, o, iMqttActionListener, iMqttMessageListener);
    }

    @Override
    public IMqttToken subscribe(String s, int i, IMqttMessageListener iMqttMessageListener) throws MqttException {
        return mqttClient.subscribe(s, i, iMqttMessageListener);
    }

    @Override
    public IMqttToken subscribe(String[] strings, int[] ints, IMqttMessageListener[] iMqttMessageListeners) throws MqttException {
        return mqttClient.subscribe(strings, ints, iMqttMessageListeners);
    }

    @Override
    public IMqttToken subscribe(String[] strings, int[] ints, Object o, IMqttActionListener iMqttActionListener, IMqttMessageListener[] iMqttMessageListeners) throws MqttException {
        return mqttClient.subscribe(strings, ints, o, iMqttActionListener, iMqttMessageListeners);
    }

    @Override
    public IMqttToken unsubscribe(String s) throws MqttException {
        return mqttClient.unsubscribe(s);
    }

    @Override
    public IMqttToken unsubscribe(String[] strings) throws MqttException {
        return mqttClient.unsubscribe(strings);
    }

    @Override
    public IMqttToken unsubscribe(String s, Object o, IMqttActionListener iMqttActionListener) throws MqttException {
        return mqttClient.unsubscribe(s, o, iMqttActionListener);
    }

    @Override
    public IMqttToken unsubscribe(String[] strings, Object o, IMqttActionListener iMqttActionListener) throws MqttException {
        return mqttClient.unsubscribe(strings, o, iMqttActionListener);
    }

    @Override
    public boolean removeMessage(IMqttDeliveryToken iMqttDeliveryToken) throws MqttException {
        return mqttClient.removeMessage(iMqttDeliveryToken);
    }

    @Override
    public void setCallback(MqttCallback mqttCallback) {
        mqttClient.setCallback(mqttCallback);
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

    @Override
    public void messageArrivedComplete(int i, int i1) throws MqttException {
        mqttClient.messageArrivedComplete(i, i1);
    }

    @Override
    public void setBufferOpts(DisconnectedBufferOptions disconnectedBufferOptions) {
        mqttClient.setBufferOpts(disconnectedBufferOptions);
    }

    @Override
    public int getBufferedMessageCount() {
        return mqttClient.getBufferedMessageCount();
    }

    @Override
    public MqttMessage getBufferedMessage(int i) {
        return mqttClient.getBufferedMessage(i);
    }

    @Override
    public void deleteBufferedMessage(int i) {
        mqttClient.deleteBufferedMessage(i);
    }

    @Override
    public int getInFlightMessageCount() {
        return mqttClient.getInFlightMessageCount();
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
