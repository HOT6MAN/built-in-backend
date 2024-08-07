package com.example.hotsix.dto.resume;

import java.util.List;

public record ResumeResponse(
        String title,
        String profileUrl,
        String position,
        List<String> techStack,
        List<ExperienceRespone> experiences,
        String comment
) {
}
