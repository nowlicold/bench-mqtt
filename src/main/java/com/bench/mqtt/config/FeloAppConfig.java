package com.bench.mqtt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * Felo APP 配置
 * </p>
 *
 * @author Karl
 * @date 2022/7/4 11:42
 */
@Configuration
@ConfigurationProperties("felosvr")
@Data
public class FeloAppConfig {
    private String appId;
    private String appSecret;
    private String feloSvrUrl;

    private String mqttUrl;
}
