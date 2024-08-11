package com.example.hotsix.dto.recruit;

import com.example.hotsix.model.Member;
import com.example.hotsix.model.Recruit;
import com.example.hotsix.model.Team;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record RecruitRequest(
        String teamId,
        @Nullable MultipartFile thumbnail,
        String domain,
        List<String> desiredPosList,
        String introduction,
        String content,
        Long authorId
) {

    public Recruit toEntity(Member author, Team team) {
        return Recruit.builder()
                .title(introduction)
                .content(content)
                .hit(0)
                .author(author)
                .team(team)
                .thumbnail(thumbnail==null?null : thumbnail.getOriginalFilename())
                .introduction(introduction)
                .domain(domain)
                .desiredPosList(desiredPosList)
                .build();
    }
}
