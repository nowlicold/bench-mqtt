package com.bench.mqtt.config.generator;

import com.bench.mqtt.config.MQTTConfig;

/**
 * <p>
 * MQTT 配置生成器
 * </p>
 *
 * @author Karl
 * @date 2022/7/1 14:40
 */
public interface MQTTConfigGenerator {
    MQTTConfig generator();
}
