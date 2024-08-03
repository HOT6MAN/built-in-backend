package com.example.hotsix.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Converter
@RequiredArgsConstructor
public class ListToJsonConverter implements AttributeConverter<List<String>, String> {

    private final ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        try {
            return objectMapper.writeValueAsString(
                    attribute.stream()
                            .map(item -> item.replaceAll("^\\[\"|\"\\]$", ""))
                            .map(item -> item.replace("\"", ""))
                            .collect(Collectors.toList())
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Unable to serialize list to JSON", e);
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Unable to deserialize list to JSON", e);
        }
    }
}

