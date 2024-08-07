package com.example.hotsix.dto.resume;

import com.example.hotsix.model.Resume;
import com.example.hotsix.model.ResumeExperience;

public record ExperienceRequest(
        String title,
        String description
) {
    public ResumeExperience toEntity(Resume resume) {
        return ResumeExperience.builder()
                .title(title)
                .description(description)
                .resume(resume)
                .build();
    }
}
