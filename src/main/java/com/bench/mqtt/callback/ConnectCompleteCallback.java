package com.bench.mqtt.callback;

/**
 * <p>
 * MQTT 连接建立成功回调
 * 一般会重新订阅 topic
 * </p>
 *
 * @author Karl
 * @date 2022/7/1 11:10
 */
public interface ConnectCompleteCallback {
    void connectComplete(boolean reconnect, String serverURI);
}
