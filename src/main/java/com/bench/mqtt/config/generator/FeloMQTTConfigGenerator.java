package com.bench.mqtt.config.generator;

import com.bench.common.exception.BenchRuntimeException;
import com.bench.httpclient.base.HttpClientFactory;
import com.bench.httpclient.base.enums.HttpMethodEnum;
import com.bench.httpclient.base.exceptions.HttpClientException;
import com.bench.httpclient.base.request.HttpStringBodyRequest;
import com.bench.httpclient.base.response.HttpResponse;
import com.bench.lang.base.json.jackson.JacksonUtils;
import com.bench.lang.base.string.utils.StringUtils;
import com.bench.mqtt.config.FeloAppConfig;
import com.bench.mqtt.config.MQTTConfig;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * Felo MQTT 配置生成器
 * </p>
 *
 * @author Karl
 * @date 2022/7/1 14:41
 */
@Component
@Lazy
@Slf4j
public class FeloMQTTConfigGenerator implements MQTTConfigGenerator {
    private final FeloAppConfig feloAppConfig;

    @Autowired
    public FeloMQTTConfigGenerator(FeloAppConfig feloAppConfig) {
        this.feloAppConfig = feloAppConfig;
    }

    @Override
    public MQTTConfig generator() {
        JsonNode jsonNode = getMqttInfo();
        if (Objects.isNull(jsonNode)){
            throw new RuntimeException("failed to generate mqtt info");
        }

        String url = JacksonUtils.getStringValue(jsonNode, "tcp_url");
        String clientId = JacksonUtils.getStringValue(jsonNode, "client_id");
        String username = JacksonUtils.getStringValue(jsonNode, "username");
        String password = JacksonUtils.getStringValue(jsonNode, "token");

        MQTTConfig mqttConfig = new MQTTConfig();
        mqttConfig.setUrl(url);
        mqttConfig.setClientId(clientId);
        mqttConfig.setUsername(username);
        mqttConfig.setPassword(password);
        return mqttConfig;
    }

    private String getAppAccessToken() {
        JsonNode jsonNode = execute("/sdk/app_access_token", feloAppConfig, "");
        if (Objects.isNull(jsonNode)){
            log.error("failed to get app access token." );
            return null;
        }
        return JacksonUtils.getStringValue(jsonNode, "app_access_token");
    }

    private JsonNode getMqttInfo() {
        String token = getAppAccessToken();
        if (Objects.isNull(token)){
            return null;
        }

        Map<String, Object> body = new HashMap<>();
        body.put("client_type", 1);
        body.put("customer_id", feloAppConfig.getAppId());
        body.put("topic", Collections.emptyList());
        body.put("topic_mode", 0);

        JsonNode jsonNode = execute("/sdk/mqtt/token/generate", body, token);
        if (Objects.isNull(jsonNode)){
            log.error("failed to get mqtt info.");
            return null;
        }

        return jsonNode.get("data");
    }

    private JsonNode execute(String api, Object body, String token) {
        HttpStringBodyRequest httpRequest = new HttpStringBodyRequest();
        httpRequest.setUrl(feloAppConfig.getFeloSvrUrl() + api);
        httpRequest.getHeaders().set("Content-Type", "application/json");
        httpRequest.getHeaders().set("Authorization", "Bearer " + token);
        httpRequest.getRequestConfig().setCharset("utf-8");
        httpRequest.setBody(JacksonUtils.toJson(body));
        httpRequest.setMethod(HttpMethodEnum.POST);
        HttpResponse httpResponse;
        try {
            httpResponse = HttpClientFactory.getInstance().getDefaultHttpClient().execute(httpRequest);
        } catch (HttpClientException e) {
            log.error("failed to send request. url={}, body={}", httpRequest.getUrl(), httpRequest.getBody(), e);
            return null;
        }

        if (httpResponse == null || httpResponse.getResponseBody() == null) {
            log.error("failed to read response. url={}, body={}", httpRequest.getUrl(), httpRequest.getBody());
            return null;
        }

        String content;
        content = new String(httpResponse.getResponseBody(), StandardCharsets.UTF_8);

        if (httpResponse.getResponseCode() != HttpStatus.SC_OK) {
            log.error("unexpected response code.code={},url={} body={}", httpResponse.getResponseCode(), httpRequest.getUrl(), content);
            return null;
        }

        JsonNode jsonNode;
        try {
            jsonNode = JacksonUtils.parseJson(content);
        } catch (BenchRuntimeException ex) {
            log.error("failed to parse response body. body={}", content, ex);
            return null;
        }

        JsonNode statusNode = jsonNode.get("status");
        String errorCode = JacksonUtils.getStringValue(statusNode, "errorCode");
        if (!StringUtils.notEquals(errorCode, "0000")) {
            log.error("failed to get app access token. status={}", content);
            return null;
        }

        return jsonNode;
    }
}
