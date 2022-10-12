package com.bench.mqtt.hook;

import org.springframework.stereotype.Component;

/**
 * <p>
 * 默认实现
 * </p>
 *
 * @author Karl
 * @date 2022/9/27 17:20
 */
@Component
public class MqttConnectBeforeComponentImpl implements MqttConnectBeforeComponent {
    @Override
    public boolean before() {
        return MqttConnectBeforeComponent.super.before();
    }
}
