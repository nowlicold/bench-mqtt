package com.bench.mqtt.client;

import com.bench.mqtt.callback.impl.*;
import com.bench.mqtt.config.generator.FeloMQTTConfigGenerator;
import com.bench.mqtt.config.generator.MQTTConfigGenerator;
import com.bench.mqtt.reconnect.impl.AsyncDefaultReconnector;
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
            DefaultConnectCompleteCallback connectCompleteCallback = new DefaultConnectCompleteCallback();
            DefaultConnectLostAsyncCallback defaultConnectLostAsyncCallback = new DefaultConnectLostAsyncCallback(new AsyncDefaultReconnector());
            DefaultDeliveryCompleteCallback deliveryCompleteCallback = new DefaultDeliveryCompleteCallback();
            DefaultMessageArrivedCallback messageArrivedCallback = new DefaultMessageArrivedCallback();

            DefaultMQTTAsyncCallback defaultMQTTAsyncCallback = new DefaultMQTTAsyncCallback(connectCompleteCallback, defaultConnectLostAsyncCallback, deliveryCompleteCallback, messageArrivedCallback);

            MQTTConfigGenerator mqttConfigGenerator = new FeloMQTTConfigGenerator();
            MQTTAsyncClient mqttAsyncClient = new MQTTAsyncClient(defaultMQTTAsyncCallback, mqttConfigGenerator);

            try {
                mqttAsyncClient.connect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }).start();

        Thread.sleep(600000);
    }
}



