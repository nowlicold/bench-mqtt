package com.bench.mqtt.client.adapter;

import com.bench.mqtt.client.ConnectClient;

/**
 * <p>
 * ConnectClientHandler 适配器接口
 * </p>
 *
 * @author Karl
 * @date 2022/8/3 11:37
 */
public interface ConnectClientAdapter {
    boolean support(ClientTypeEnum clientType);
    ConnectClient get();
}
