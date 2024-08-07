package com.example.hotsix.editor;

import com.example.hotsix.dto.resume.ExperienceRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.beans.PropertyEditorSupport;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ExperienceRequestPropertyEditor extends PropertyEditorSupport {

    private final Converter<String, List<ExperienceRequest>> converter;

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(converter.convert(text));
    }
}
