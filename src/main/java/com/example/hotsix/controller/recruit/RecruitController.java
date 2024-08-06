package com.example.hotsix.controller.recruit;

import com.example.hotsix.dto.recruit.RecruitRequest;
import com.example.hotsix.dto.recruit.RecruitResponse;
import com.example.hotsix.dto.recruit.RecruitShortResponse;
import com.example.hotsix.editor.RecruitPropertyEditor;
import com.example.hotsix.model.Member;
import com.example.hotsix.model.Recruit;
import com.example.hotsix.model.Team;
import com.example.hotsix.service.recruit.RecruitService;
import com.example.hotsix.service.storage.StorageService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Provider;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
public class RecruitController {

    private final RecruitService recruitService;
    private final StorageService storageService;
    private final Provider<RecruitPropertyEditor> recruitPropertyEditorProvider;

    public RecruitController(RecruitService recruitService,
                      @Qualifier("fileSystemStorageService") StorageService storageService,
                      Provider<RecruitPropertyEditor> recruitPropertyEditorProvider) {
        this.recruitService = recruitService;
        this.storageService = storageService;
        this.recruitPropertyEditorProvider = recruitPropertyEditorProvider;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Recruit.class, recruitPropertyEditorProvider.get());
    }

    @GetMapping("/teambuilding/recruit/{id}")
    public RecruitResponse recruit(@PathVariable("id") Recruit recruit) {
        recruit.hit();
        recruitService.save(recruit);

        return recruitService.toResponse(recruit);
    }

    @GetMapping("/teambuilding/recruits")
    public List<RecruitShortResponse> recruits(
            @RequestParam(value = "teamName", required = false) String teamName,
            @RequestParam(value = "authorName", required = false) String authorName,
            @RequestParam(value = "desiredPos", required = false) String desiredPos
    ) throws InterruptedException {
        Thread.sleep(1000*3);

        if (teamName != null) {
            return recruitService.findSummaryAllByTeamName(teamName);
        } else if (authorName != null) {
            return recruitService.findSummaryAllByAuthorName(authorName);
        } else if (desiredPos != null) {
            return recruitService.findSummaryAllByDesiredPos(desiredPos);
        }

        return recruitService.findSummaryAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/teambuilding/recruit")
    public void registerRecruit(@ModelAttribute RecruitRequest recruitRequest /*, @AuthenticationPrincipal Member author */) throws IOException {
        // TODO: @AuthenticationPrincipal에서 받기
//        Member testMember = new Member(1L, "ssafy", "ssafy@ssafy.com", "ssafy", "X", "010-1111-2222", "addr", "X", "X", null);
        //Member testMember = new Member(1L, "ssafy", "ssafy@ssafy.com", "ssafy", "X", "010-1111-2222", "addr", "X", "X", null);

        // TODO: team 가져오기
//        Team recruitingTeam = new Team(1L, "hot6man", "X", "Hello hot6man", LocalDateTime.now(), LocalDateTime.now(), "X", "X",  null, null, null);

//        Recruit newRecruit = recruitRequest.toEntity(testMember, recruitingTeam);

//        recruitService.save(newRecruit);
        storageService.store(recruitRequest.thumbnail());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/teambuilding/recruit/{id}")
    public void updateRecruit(@PathVariable("id") Recruit recruit, @ModelAttribute RecruitRequest recruitRequest) throws IOException {
        String storedThumbnailName = recruit.getThumbnail();
        MultipartFile uploadedThumbnail = recruitRequest.thumbnail();

        if (storedThumbnailName == null && uploadedThumbnail != null) {
            storageService.store(uploadedThumbnail);
        } else if (storedThumbnailName != null && uploadedThumbnail == null) {
            storageService.remove(storedThumbnailName);
        } else if (storedThumbnailName != null && uploadedThumbnail != null) {
            storageService.replace(storedThumbnailName, uploadedThumbnail);
        }

        recruit.update(recruitRequest);
        recruitService.save(recruit);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/teambuilding/recruit/{id}")
    public void deleteRecruit(@PathVariable("id") Recruit recruit) throws IOException {
        storageService.remove(recruit.getThumbnail());

        recruitService.delete(recruit);
    }

    @GetMapping("/team")
    public List<Map<Long, String>> myTeams(/*, @AuthenticationPrincipal Member me */) {
        // TODO: 내가 속한 팀 가져오기

        return Arrays.asList(
                Map.of(1L, "hot6man"),
                Map.of(2L, "hot6man2"),
                Map.of(3L, "hot6man3"),
                Map.of(4L, "hot6man4")
        );
    }
}
