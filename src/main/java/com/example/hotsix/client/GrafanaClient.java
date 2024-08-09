package com.example.hotsix.client;

import com.example.hotsix.util.JsonUtil;
import com.example.hotsix.util.RandomUtil;
import com.example.hotsix.util.TimeUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
public class GrafanaClient {
    @Value("${grafana.url}")
    private String grafanaUrl;
    @Value("${grafana.admin-token}")
    private String adminToken;

    private final JsonUtil jsonUtil;
    private final BuiltInWebClient builtInWebClient;

    public String addGrafanaDashboard(int serviceNum) throws Exception {
        String createDashBoardUrl = grafanaUrl + "/api/dashboards/import";

        JsonNode jsonNode = jsonUtil.readJsonFile("/grafana-datasource.json");
        System.out.println("jsonNode = " + jsonNode);

        if (jsonNode.has("dashboard") && jsonNode.get("dashboard").isObject()) {
            System.out.println("여기 실행되나요");
            String uId = this.generateOrderId();
            String title = "Monitoring Nginx Server - Service Metric-" + uId;
            System.out.println("uId = " + uId);
            ObjectNode dashBoardNode = (ObjectNode) jsonNode.get("dashboard");
            dashBoardNode.put("title", title);
            dashBoardNode.put("uId", uId);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBearerAuth(adminToken);
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

            String response = builtInWebClient.post(createDashBoardUrl, jsonNode, httpHeaders);
            // Process response
            System.out.println("response = " + response);

            return uId;
        }

        return null;
    }

    private String generateOrderId() {
        return String.format("%s%s",
                TimeUtil.convertEpochToDateString(TimeUtil.getCurrentTimeMillisSeoul(), "yyMMdd"),
                RandomUtil.generateRandomString());
    }

}
