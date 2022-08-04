package com.bench.mqtt.client;

import com.bench.mqtt.callback.impl.*;
import com.bench.mqtt.config.FeloAppConfig;
import com.bench.mqtt.config.generator.FeloMQTTConfigGenerator;
import com.bench.mqtt.config.generator.MQTTConfigGenerator;
import com.bench.mqtt.reconnect.impl.AsyncDefaultReconnector;
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
public class MQTTAsyncClientTest {
    @Test
    public void test() throws InterruptedException {

        new Thread(() -> {
            DefaultMQTTAsyncCallback defaultMQTTAsyncCallback = new DefaultMQTTAsyncCallback(new AsyncDefaultReconnector());

            FeloAppConfig feloAppConfig = new FeloAppConfig();
            feloAppConfig.setAppId("yd996720A2600649649D836C46E41FBB0A");
            feloAppConfig.setAppSecret("X9IVnzPwvGNd8Jz7UamT7BpvqVOckz2jfiEx3y2vnd4=");
            feloAppConfig.setFeloSvrUrl("https://open.felo.me");

            MQTTConfigGenerator mqttConfigGenerator = new FeloMQTTConfigGenerator(feloAppConfig);
            MQTTAsyncClient mqttAsyncClient = new MQTTAsyncClient(defaultMQTTAsyncCallback, mqttConfigGenerator);

            try {
                IMqttToken token = mqttAsyncClient.connect();
                token.waitForCompletion();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }).start();

        Thread.sleep(600000);
    }
}



