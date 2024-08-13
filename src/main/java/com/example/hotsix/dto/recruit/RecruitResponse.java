package com.example.hotsix.dto.recruit;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record RecruitResponse(
        String thumbnailUrl,
        Long teamId,
        String teamName,
        Integer hit,
        Long authorId,
        String authorName,
        String domain,
        List<String> desiredPosList,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdDate,
        String introduction,
        String content
) {
}
