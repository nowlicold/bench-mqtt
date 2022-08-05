package com.bench.mqtt;

import com.bench.mqtt.client.adapter.ClientTypeEnum;
import com.bench.mqtt.client.ConnectClient;
import com.bench.mqtt.client.adapter.ConnectClientAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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
public class MQTTConnectComponent implements ApplicationRunner {
    private final ConnectClientAdapter connectClientAdapter;

    @Autowired
    public MQTTConnectComponent(ConnectClientAdapter connectClientAdapter) {
        this.connectClientAdapter = connectClientAdapter;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("Connecting to MQTT");
        new Thread(() -> {
            try {
                ConnectClient connectClient = connectClientAdapter.get();
                connectClient.connect();
            } catch (Exception e) {
                log.warn("Failed to connect MQTT", e);
            }
        }).start();
    }
}
