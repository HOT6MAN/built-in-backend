package com.example.hotsix.converter;

import com.example.hotsix.dto.resume.ExperienceRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StringToExperienceRequestListConverter implements Converter<String, List<ExperienceRequest>> {

    private final ObjectMapper objectMapper;

    @Override
    public List<ExperienceRequest> convert(String source) {
        try {
            return objectMapper.readValue(source, new TypeReference<List<ExperienceRequest>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

