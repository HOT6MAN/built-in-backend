package com.example.hotsix.client;

import com.example.hotsix.util.JsonUtil;
import com.example.hotsix.util.RandomUtil;
import com.example.hotsix.util.TimeUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
@Slf4j
public class GrafanaClient {
    @Value("${grafana.url}")
    private String grafanaUrl;
    @Value("${grafana.admin-token}")
    private String adminToken;

    private final JsonUtil jsonUtil;
    private final BuiltInWebClient builtInWebClient;

    public String addGrafanaDashboard(Long serviceNum) throws Exception {
        String createDashBoardUrl = grafanaUrl + "/api/dashboards/import";

        JsonNode jsonNode = jsonUtil.readJsonFile("/grafana-datasource.json");
        log.info("jsonNode = {}", jsonNode);

        if (jsonNode.has("dashboard") && jsonNode.get("dashboard").isObject()) {
            log.info("여기 실행되나요");
            String uId = this.generateOrderId();
            String title = "Monitoring Nginx Server - Service Metric-" + uId;
            System.out.println("uId = " + uId);

            ObjectNode dashBoardNode = (ObjectNode) jsonNode.get("dashboard");
            dashBoardNode.put("title", title);
            dashBoardNode.put("uId", uId);

            // NGINX_INSTANCE 환경 변수와 쿼리 업데이트
            updateNginxInstanceInQueries(dashBoardNode, serviceNum, uId);

            System.out.println("dashBoardNode = " + dashBoardNode);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBearerAuth(adminToken);
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

            String response = builtInWebClient.post(createDashBoardUrl, jsonNode, httpHeaders);

            // 응답 처리
            if (response != null) {
                log.info("grafana response = {}", response);
            } else {
                log.error("Grafana API 호출 후 응답이 없습니다.");
            }

            return uId;
        }

        return null;
    }

    public String getGrafanaUrlByUID(String uid) throws Exception {
        String getGrafanaInfoUrl = String.format("%s/api/search?tag=%s", grafanaUrl, uid);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(adminToken);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        JsonNode response = builtInWebClient.get(getGrafanaInfoUrl, httpHeaders)
                .bodyToMono(JsonNode.class)
                .block();

        // 첫 번째 원소의 url 값을 반환
        if (response.isArray() && response.size() > 0) {
            return response.get(0).get("url").asText();
        } else {
            throw new Exception("Grafana dashboard not found for the given UID: " + uid);
        }
    }

    private void updateNginxInstanceInQueries(ObjectNode dashBoardNode, Long serviceNum, String uId) {
        // 쿼리에서 NGINX_INSTANCE 값을 serviceNum으로 업데이트
        String instanceValue = "nginx_SN-" + serviceNum;

        // 예시: targets 필드를 포함한 쿼리를 순회하며 수정하는 로직 구현
        if (dashBoardNode.has("panels")) {
            for (JsonNode panel : dashBoardNode.get("panels")) {
                if (panel.has("targets")) {
                    for (JsonNode target : panel.get("targets")) {
                        if (target.has("expr")) {
                            String expr = target.get("expr").asText();
                            expr = expr.replace("$NGINX_INSTANCE", instanceValue);  // NGINX_INSTANCE 환경변수 업데이트
                            ((ObjectNode) target).put("expr", expr);
                        }
                    }
                }
            }
        }

        // tags: [] 안에 tags: [{uId}] 추가
        if (dashBoardNode.has("tags")) {
            ArrayNode tagsArray = (ArrayNode) dashBoardNode.get("tags");
            // uId를 태그 배열에 추가
            tagsArray.add(uId);
        }
    }

    private String generateOrderId() {
        return String.format("%s%s",
                TimeUtil.convertEpochToDateString(TimeUtil.getCurrentTimeMillisSeoul(), "yyMMdd"),
                RandomUtil.generateRandomString());
    }

}
