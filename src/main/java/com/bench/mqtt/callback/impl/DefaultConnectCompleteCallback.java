package com.bench.mqtt.callback.impl;

import com.bench.mqtt.callback.ConnectCompleteCallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p>
 * MQTT 连接成功后 默认回调处理
 * </p>
 *
 * @author Karl
 * @date 2022/7/1 11:30
 */
@Component
@Slf4j
public class DefaultConnectCompleteCallback implements ConnectCompleteCallback {
    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        log.info("MQTT connected");
    }
}
