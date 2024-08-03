package com.example.hotsix.service.recruit;

import com.example.hotsix.dto.recruit.RecruitResponse;
import com.example.hotsix.dto.recruit.RecruitShortResponse;
import com.example.hotsix.exception.BuiltInException;
import com.example.hotsix.model.Recruit;
import com.example.hotsix.properties.ServerProperties;
import com.example.hotsix.properties.StorageProperties;
import com.example.hotsix.repository.recruit.RecruitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.example.hotsix.enums.Process.RECRUIT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class RecruitService {

    private final RecruitRepository recruitRepository;
    private final ServerProperties serverProperties;
    private final StorageProperties storageProperties;

    public Recruit findById(Long id) {
        return recruitRepository.findById(id).orElseThrow(() -> new BuiltInException(RECRUIT_NOT_FOUND));
    }

    public void save(Recruit recruit) {
        recruitRepository.save(recruit);
    }

    public List<Recruit> findAll() {
        return recruitRepository.findAll();
    }

    public List<RecruitShortResponse> findSummaryAll() {
        return findAll().stream().map(this::toShortResponse).collect(Collectors.toList());
    }

    public List<RecruitShortResponse> findSummaryAllByTeamName(String teamName) {
        return recruitRepository.findRecruitsByTeamName(teamName).stream().map(this::toShortResponse).collect(Collectors.toList());
    }

    public List<RecruitShortResponse> findSummaryAllByAuthorName(String authorName) {
        return recruitRepository.findRecruitsByAuthorName(authorName).stream().map(this::toShortResponse).collect(Collectors.toList());
    }

    public List<RecruitShortResponse> findSummaryAllByDesiredPos(String desiredPos) {
        return recruitRepository.findRecruitsByDesiredPos("\"" + desiredPos + "\"").stream().map(this::toShortResponse).collect(Collectors.toList());
    }

    public RecruitResponse toResponse(Recruit recruit) {
        return new RecruitResponse(
                getThumbnailUrl(recruit),
                recruit.getTeam().getId(),
                recruit.getTeam().getName(),
                recruit.getHit(),
                recruit.getAuthor().getName(),
                recruit.getDomain(),
                recruit.getDesiredPosList(),
                recruit.getCreatedDate(),
                recruit.getIntroduction(),
                recruit.getContent()
        );
    }

    public RecruitShortResponse toShortResponse(Recruit recruit) {
        return new RecruitShortResponse(
                recruit.getId(),
                recruit.getTeam().getName(),
                recruit.getHit(),
                getThumbnailUrl(recruit),
                recruit.getIntroduction(),
                recruit.getDomain(),
                recruit.getDesiredPosList(),
                recruit.getAuthor().getName(),
                recruit.getCreatedDate().toLocalDate()
        );
    }

    private String getThumbnailUrl(Recruit recruit) {
        Pattern pattern = Pattern.compile("src/main/resources/static/(.*)");
        Matcher matcher = pattern.matcher(storageProperties.uploadImages());

        matcher.matches();
        String thumbnailUrl = String.format("%s/%s/%s", serverProperties.origin(), matcher.group(1), recruit.getThumbnail());
        return thumbnailUrl;
    }

    public void delete(Recruit recruit) {
        recruitRepository.delete(recruit);
    }
}
