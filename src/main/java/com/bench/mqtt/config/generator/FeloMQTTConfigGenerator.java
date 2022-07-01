package com.bench.mqtt.config.generator;

import com.bench.mqtt.config.MQTTConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * <p>
 * Felo MQTT 配置生成器
 * </p>
 *
 * @author Karl
 * @date 2022/7/1 14:41
 */
@Component
@Lazy
public class FeloMQTTConfigGenerator implements MQTTConfigGenerator {
    @Value("${felosvr.appId}")
    private String appId;
    @Value("${felosvr.appSecret}")
    private String appSecret;

    @Override
    public MQTTConfig generator() {
        // 使用 appId 和 appSecret 调用 Felo app 服务，获取 app_token
        // 使用 app_token 获取连接参数
        // new MQTTConnectConfig()

        MQTTConfig mqttConfig = new MQTTConfig();
        mqttConfig.setUrl("ssl://mqtt.felo.me:8883");
        mqttConfig.setClientId("000c45f2a36f495db66d8992765625ab");
        mqttConfig.setUsername("ca43a499a6dc98af130311a37a0f062e");
        mqttConfig.setPassword("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpbmZvIjp7InNlcnZlcl9pZCI6InlkOTk2NzIwQTI2MDA2NDk2NDlEODM2QzQ2RTQxRkJCMEEiLCJzZXJ2ZXJfdHlwZSI6MiwiY2xpZW50X3R5cGUiOjUsImN1c3RvbWVyX2lkIjoiY2E0M2E0OTlhNmRjOThhZjEzMDMxMWEzN2EwZjA2MmUiLCJjbGllbnRfaWQiOiIwMDBjNDVmMmEzNmY0OTVkYjY2ZDg5OTI3NjU2MjVhYiIsInVzZXJuYW1lIjoiY2E0M2E0OTlhNmRjOThhZjEzMDMxMWEzN2EwZjA2MmUifSwiZXhwIjoxNjU2Njc5Mjc2fQ.8q5_lMiAN9KwVdVTqU3dw9L7sK3zu9IwskDoOtSEQrA");
        return mqttConfig;
    }
}
