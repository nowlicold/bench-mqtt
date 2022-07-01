package com.bench.mqtt.config;

import lombok.Data;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

/**
 * <p>
 * MQTT 必要配置
 * </p>
 *
 * @author Karl
 * @date 2022/7/1 12:04
 */
@Data
public class MQTTConfig {

    private String url = "ssl://mqtt.felo.me:8883";
    private String clientId;

    private String username;
    private String password;

    // 默认心跳 60s，在连接 60s 后会断开连接
    private int keepAliveInterval = MqttConnectOptions.CONNECTION_TIMEOUT_DEFAULT;
    private int connectionTimeout = MqttConnectOptions.CONNECTION_TIMEOUT_DEFAULT;
    private boolean cleanSession = MqttConnectOptions.CLEAN_SESSION_DEFAULT;
}
