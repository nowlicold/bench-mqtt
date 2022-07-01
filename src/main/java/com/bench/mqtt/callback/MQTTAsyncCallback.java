package com.bench.mqtt.callback;

/**
 * <p>
 * 异步 MQTT 客户端回调接口聚合
 * </p>
 *
 * @author Karl
 * @date 2022/7/1 17:36
 */
public interface MQTTAsyncCallback extends ConnectCompleteCallback, ConnectLostAsyncCallback, MessageArrivedCallback, DeliveryCompleteCallback {

}
