package com.bench.mqtt.client;

import com.bench.mqtt.callback.impl.DefaultMQTTCallback;
import com.bench.mqtt.callback.impl.DefaultConnectCompleteCallback;
import com.bench.mqtt.callback.impl.DefaultConnectLostCallback;
import com.bench.mqtt.callback.impl.DefaultDeliveryCompleteCallback;
import com.bench.mqtt.callback.impl.DefaultMessageArrivedCallback;
import com.bench.mqtt.config.generator.FeloMQTTConfigGenerator;
import com.bench.mqtt.config.generator.MQTTConfigGenerator;
import com.bench.mqtt.reconnect.impl.DefaultReconnector;
import org.eclipse.paho.client.mqttv3.MqttException;
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
            DefaultConnectCompleteCallback connectCompleteCallback = new DefaultConnectCompleteCallback();
            DefaultConnectLostCallback connectLostCallback = new DefaultConnectLostCallback(new DefaultReconnector());
            DefaultDeliveryCompleteCallback deliveryCompleteCallback = new DefaultDeliveryCompleteCallback();
            DefaultMessageArrivedCallback messageArrivedCallback = new DefaultMessageArrivedCallback();

            DefaultMQTTCallback defaultMqttCallback = new DefaultMQTTCallback(connectCompleteCallback, connectLostCallback, deliveryCompleteCallback, messageArrivedCallback);

            MQTTConfigGenerator mqttConfigGenerator = new FeloMQTTConfigGenerator();
            MQTTClient mqttClient = new MQTTClient(defaultMqttCallback, mqttConfigGenerator);

            try {
                mqttClient.connect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }).start();

        Thread.sleep(600000);
    }
}



