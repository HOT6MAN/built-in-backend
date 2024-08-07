package com.example.hotsix.dto.resume;

import java.time.LocalDate;

public record ResumeShortResponse(
        Long id,
        String title,
        LocalDate updatedDate,
        String profileUrl
) {
}
