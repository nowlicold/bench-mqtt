package com.bench.mqtt.client;

import com.bench.mqtt.callback.impl.*;
import com.bench.mqtt.config.FeloAppConfig;
import com.bench.mqtt.config.generator.FeloMqttConfigGenerator;
import com.bench.mqtt.config.generator.MqttConfigGenerator;
import com.bench.mqtt.connect.impl.DefaultReconnector;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.Test;

/**
 * <p>
 * MQTTAsyncClientTest
 * </p>
 *
 * @author Karl
 * @date 2022/7/1 15:46
 */
public class AsyncMqttClientTest {
    @Test
    public void test() throws InterruptedException {

        new Thread(() -> {
            DefaultAsyncMqttCallback defaultAsyncMqttCallback = new DefaultAsyncMqttCallback(new DefaultReconnector());

            FeloAppConfig feloAppConfig = new FeloAppConfig();
            feloAppConfig.setAppId("yd9DDF0880AB034146AB3C73A10F6ED62A");
            feloAppConfig.setAppSecret("H2H7oJEJOJEjWAV06ETVvf1bNrA+ipkX6vQc+ijbRYY=");
            feloAppConfig.setFeloSvrUrl("https://api.sandbox.felo.me");

            MqttConfigGenerator mqttConfigGenerator = new FeloMqttConfigGenerator(feloAppConfig);
            AsyncMqttClient asyncMqttClient = new AsyncMqttClient(defaultAsyncMqttCallback, mqttConfigGenerator);

            try {
                IMqttToken token = asyncMqttClient.connect();
                token.waitForCompletion();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }).start();

        Thread.sleep(600000);
    }
}



