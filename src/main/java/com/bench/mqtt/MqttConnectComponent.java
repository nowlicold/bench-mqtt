package com.bench.mqtt;

import com.bench.mqtt.connect.ConnectClient;
import com.bench.mqtt.connect.adapter.ConnectClientAdapter;
import lombok.extern.slf4j.Slf4j;
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
    private final ConnectClientAdapter connectClientAdapter;

    @Autowired
    public MqttConnectComponent(ConnectClientAdapter connectClientAdapter) {
        this.connectClientAdapter = connectClientAdapter;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("Connecting to MQTT");
        new Thread(() -> {
            try {
                ConnectClient connectClient = connectClientAdapter.getAdapter();
                connectClient.connect();
            } catch (Exception e) {
                log.warn("Failed to connect MQTT", e);
            }
        }).start();
    }
}