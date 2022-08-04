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
    private final String clientType;
    private final List<ConnectClientAdapter> connectClientAdapters;

    @Autowired
    public MQTTConnectComponent(List<ConnectClientAdapter> connectClientAdapters, @Value("${mqtt.client:DEFAULT}") String clientType) {
        this.connectClientAdapters = connectClientAdapters;
        this.clientType = clientType;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("Connecting to MQTT");
        new Thread(() -> {
            try {
                Optional<ConnectClientAdapter> first = connectClientAdapters.stream()
                        .filter(connectClientAdapter -> connectClientAdapter.support(ClientTypeEnum.valueOf(clientType)))
                        .findFirst();
                if (!first.isPresent()) {
                    log.error("Not found a valid MQTT client type, clientType={}", this.clientType);
                    return;
                }
                ConnectClient connectClient = first.get().get();
                connectClient.connect();
            } catch (Exception e) {
                log.warn("Failed to connect MQTT", e);
            }
        }).start();
    }
}
