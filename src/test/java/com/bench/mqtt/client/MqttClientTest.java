package com.bench.mqtt.client;

import com.bench.mqtt.callback.impl.DefaultMqttCallback;
import com.bench.mqtt.config.FeloAppConfig;
import com.bench.mqtt.config.generator.FeloMqttConfigGenerator;
import com.bench.mqtt.config.generator.MqttConfigGenerator;
import com.bench.mqtt.connect.impl.DefaultReconnector;
import org.junit.Test;

/**
 * <p>
 * MQTTClientTest
 * </p>
 *
 * @author Karl
 * @date 2022/7/1 15:46
 */
public class MqttClientTest {
    @Test
    public void test() throws InterruptedException {
        new Thread(() -> {
            DefaultMqttCallback defaultMqttCallback = new DefaultMqttCallback(new DefaultReconnector());

            FeloAppConfig feloAppConfig = new FeloAppConfig();
            feloAppConfig.setAppId("yd9DDF0880AB034146AB3C73A10F6ED62A");
            feloAppConfig.setAppSecret("H2H7oJEJOJEjWAV06ETVvf1bNrA+ipkX6vQc+ijbRYY=");
            feloAppConfig.setFeloSvrUrl("https://api.sandbox.felo.me");
            MqttConfigGenerator mqttConfigGenerator = new FeloMqttConfigGenerator(feloAppConfig);

            MqttClient mqttClient = new MqttClient(defaultMqttCallback, mqttConfigGenerator);
            try {
                mqttClient.connect();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();

        Thread.sleep(600000);
    }
}



