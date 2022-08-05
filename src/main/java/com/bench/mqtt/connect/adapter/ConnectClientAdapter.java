package com.bench.mqtt.connect.adapter;

import com.bench.mqtt.connect.ConnectClient;

/**
 * <p>
 * ConnectClientHandler 适配器接口
 * </p>
 *
 * @author Karl
 * @date 2022/8/3 11:37
 */
public interface ConnectClientAdapter {
    ConnectClient getAdapter();
}
