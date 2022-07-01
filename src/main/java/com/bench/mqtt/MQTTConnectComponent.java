package com.bench.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 项目启动时，自动连接 MQTT 服务
 * </p>
 *
 * @author Karl
 * @date 2022/6/29 18:12
 */
@Component
@Slf4j
public class MQTTConnectComponent implements ApplicationRunner {

	@Override
	public void run(ApplicationArguments args) {
		/*new Thread(() -> {
			try {
				connectionClientComponent.connect();
			} catch (Exception e) {
				log.warn("Failed to connect MQTT", e);
			}
		}).start();*/
	}
}
