package com.example.hotsix.dto.resume;

import com.example.hotsix.model.Member;
import com.example.hotsix.model.Resume;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public record ResumeRequest(
        String title,
        MultipartFile profile,
        String position,
        List<String> techStack,
        List<ExperienceRequest> experiences,
        String comment
) {
    public Resume toEntity(Member author) {
        return Resume.builder()
                .title(title)
                .profile(profile.getOriginalFilename())
                .position(position)
                .techStack(techStack)
                .experiences(new ArrayList<>())
                .comment(comment)
                .author(author)
                .build();
    }
}
