package com.example.hotsix.dto.recruit;

import java.time.LocalDate;
import java.util.List;

public record RecruitShortResponse(
        Long id,
        String teamName,
        Integer hit,
        String thumbnailUrl,
        String introduction,
        String domain,
        List<String> desiredPositions,
        String authorName,
        LocalDate createdDate
) {
}
