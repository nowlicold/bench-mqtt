package com.bench.mqtt.client;

import com.bench.mqtt.callback.impl.DefaultMQTTCallback;
import com.bench.mqtt.config.FeloAppConfig;
import com.bench.mqtt.config.generator.FeloMQTTConfigGenerator;
import com.bench.mqtt.config.generator.MQTTConfigGenerator;
import com.bench.mqtt.reconnect.impl.DefaultReconnector;
import org.junit.Test;

/**
 * <p>
 * MQTTClientTest
 * </p>
 *
 * @author Karl
 * @date 2022/7/1 15:46
 */
public class MQTTClientTest {
    @Test
    public void test() throws InterruptedException {
        new Thread(() -> {
            DefaultMQTTCallback defaultMqttCallback = new DefaultMQTTCallback(new DefaultReconnector());

            FeloAppConfig feloAppConfig = new FeloAppConfig();
            feloAppConfig.setAppId("yd996720A2600649649D836C46E41FBB0A");
            feloAppConfig.setAppSecret("X9IVnzPwvGNd8Jz7UamT7BpvqVOckz2jfiEx3y2vnd4=");
            feloAppConfig.setFeloSvrUrl("https://open.felo.me");
            MQTTConfigGenerator mqttConfigGenerator = new FeloMQTTConfigGenerator(feloAppConfig);

            MQTTClient mqttClient = new MQTTClient(defaultMqttCallback, mqttConfigGenerator);
            try {
                mqttClient.connect();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();

        Thread.sleep(600000);
    }
}



