package com.bench.mqtt;

import com.bench.mqtt.client.MqttClient;
import com.bench.mqtt.hook.MqttConnectBeforeComponent;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 项目启动时，自动连接 MQTT 服务
 * </p>
 *
 * @author Karl
 * @date 2022/6/29 18:12
 */
@Component
@Slf4j
public class MqttConnectComponent implements ApplicationRunner {
    private final MqttClient mqttClient;
    private final MqttConnectBeforeComponent mqttConnectBeforeComponent;

    @Autowired
    public MqttConnectComponent(MqttClient mqttClient, MqttConnectBeforeComponent mqttConnectBeforeComponent) {
        this.mqttClient = mqttClient;
        this.mqttConnectBeforeComponent = mqttConnectBeforeComponent;
    }

    @Override
    public void run(ApplicationArguments args) throws MqttException {
        if (mqttConnectBeforeComponent.before()) {
            log.info("Connecting to MQTT");
            mqttClient.connect();
        }
    }
}
