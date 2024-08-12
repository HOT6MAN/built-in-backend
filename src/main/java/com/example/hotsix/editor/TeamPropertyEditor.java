package com.example.hotsix.editor;

import com.example.hotsix.service.team.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.beans.PropertyEditorSupport;

@Component
@RequiredArgsConstructor
public class TeamPropertyEditor extends PropertyEditorSupport {

    private final TeamService teamService;

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        Long teamId = Long.valueOf(text);

        setValue(teamService.findById(teamId));
    }
}
