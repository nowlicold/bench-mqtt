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
public class MqttConfig {

    private String url = "ssl://mqtt.felo.me:8883";
    private String clientId;

    private String username;
    private String password;

    private int keepAliveInterval = 5;
    private int connectionTimeout = MqttConnectOptions.CONNECTION_TIMEOUT_DEFAULT;
    private boolean cleanSession = MqttConnectOptions.CLEAN_SESSION_DEFAULT;
}
