package com.bench.mqtt.hook;

/**
 * <p>
 * 连接前调用的钩子接口
 * </p>
 *
 * @author Karl
 * @date 2022/9/23 14:23
 */
public interface MqttConnectBeforeComponent {
    default boolean before() {
        return true;
    }
}
