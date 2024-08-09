package com.example.hotsix.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class JsonUtil {
    private final ObjectMapper objectMapper;

    // resources 폴더 내부에 있는 file을 읽어들여서, JsonNode로 변환하는 메서드
    public JsonNode readJsonFile(String filePath) throws Exception {
        // Read the JSON file from resources
        InputStream inputStream = getClass().getResourceAsStream(filePath);
        if (inputStream == null) {
            throw new IllegalArgumentException("File not found: " + filePath);
        }

        // Convert InputStream to String
        String jsonContent = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        // Parse the JSON content to JsonNode
        return objectMapper.readTree(jsonContent);
    }
}
