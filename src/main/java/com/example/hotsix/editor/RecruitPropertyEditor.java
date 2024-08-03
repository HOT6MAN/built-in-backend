package com.example.hotsix.editor;

import com.example.hotsix.service.recruit.RecruitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.beans.PropertyEditorSupport;

@Component
@RequiredArgsConstructor
public class RecruitPropertyEditor extends PropertyEditorSupport {

    private final RecruitService recruitService;

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        long id = Long.parseLong(text);

        setValue(recruitService.findById(id));
    }
}
