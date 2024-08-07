package com.example.hotsix.editor;

import com.example.hotsix.service.resume.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.beans.PropertyEditorSupport;

@Component
@RequiredArgsConstructor
public class ResumePropertyEditor extends PropertyEditorSupport {
    private final ResumeService resumeService;

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        long id = Long.parseLong(text);

        setValue(resumeService.findById(id));
    }
}
