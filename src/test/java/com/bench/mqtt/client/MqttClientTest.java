package com.bench.mqtt.client;

import com.bench.mqtt.callback.MqttCallback;
import com.bench.mqtt.callback.impl.MqttCallbackImpl;
import com.bench.mqtt.client.impl.MqttAsyncClientImpl;
import com.bench.mqtt.client.impl.MqttClientImpl;
import com.bench.mqtt.config.FeloAppConfig;
import com.bench.mqtt.config.generator.FeloMqttConfigGenerator;
import com.bench.mqtt.config.generator.MqttConfigGenerator;
import com.bench.mqtt.connect.impl.DefaultReconnector;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;


public class MqttClientTest {

    @Test
    public void test1() {
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        new Thread(() -> {
            MqttCallback callback = new MqttCallbackImpl(new DefaultReconnector());

            FeloAppConfig feloAppConfig = new FeloAppConfig();
            feloAppConfig.setAppId("yd9DDF0880AB034146AB3C73A10F6ED62A");
            feloAppConfig.setAppSecret("H2H7oJEJOJEjWAV06ETVvf1bNrA+ipkX6vQc+ijbRYY=");
            feloAppConfig.setFeloSvrUrl("https://api.sandbox.felo.me");
            MqttConfigGenerator mqttConfigGenerator = new FeloMqttConfigGenerator(feloAppConfig);

            MqttClient mqttClient = new MqttClientImpl(callback, mqttConfigGenerator);
            try {
                mqttClient.connect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        try {
            countDownLatch.await();//需要捕获异常，当其中线程数为0时这里才会继续运行
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        new Thread(() -> {
            MqttCallback callback = new MqttCallbackImpl(new DefaultReconnector());

            FeloAppConfig feloAppConfig = new FeloAppConfig();
            feloAppConfig.setAppId("yd9DDF0880AB034146AB3C73A10F6ED62A");
            feloAppConfig.setAppSecret("H2H7oJEJOJEjWAV06ETVvf1bNrA+ipkX6vQc+ijbRYY=");
            feloAppConfig.setFeloSvrUrl("https://api.sandbox.felo.me");
            MqttConfigGenerator mqttConfigGenerator = new FeloMqttConfigGenerator(feloAppConfig);

            MqttClient mqttClient = new MqttAsyncClientImpl(callback, mqttConfigGenerator);
            try {
                mqttClient.connect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        try {
            countDownLatch.await();//需要捕获异常，当其中线程数为0时这里才会继续运行
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }
}
