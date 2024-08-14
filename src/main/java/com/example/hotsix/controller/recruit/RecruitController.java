package com.example.hotsix.controller.recruit;

import com.example.hotsix.dto.recruit.RecruitRequest;
import com.example.hotsix.dto.recruit.RecruitResponse;
import com.example.hotsix.dto.recruit.RecruitShortResponse;
import com.example.hotsix.dto.team.TeamShortResponse;
import com.example.hotsix.editor.RecruitPropertyEditor;
import com.example.hotsix.editor.TeamPropertyEditor;
import com.example.hotsix.model.*;
import com.example.hotsix.oauth.dto.CustomOAuth2User;
import com.example.hotsix.service.member.MemberService;
import com.example.hotsix.service.recruit.RecruitService;
import com.example.hotsix.service.storage.StorageService;
import com.example.hotsix.service.team.TeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import java.util.List;

@RestController
@Slf4j
public class RecruitController {

    private final MemberService memberService;
    private final TeamService teamService;
    private final RecruitService recruitService;
    private final StorageService storageService;
    private final Provider<RecruitPropertyEditor> recruitPropertyEditorProvider;
    private final Provider<TeamPropertyEditor> teamPropertyEditorProvider;

    public RecruitController(MemberService memberService,
                             TeamService teamService,
                             RecruitService recruitService,
                             @Qualifier("fileSystemStorageService") StorageService storageService,
                             Provider<RecruitPropertyEditor> recruitPropertyEditorProvider,
                             Provider<TeamPropertyEditor> teamPropertyEditorProvider) {
        this.memberService = memberService;
        this.teamService = teamService;
        this.recruitService = recruitService;
        this.storageService = storageService;
        this.recruitPropertyEditorProvider = recruitPropertyEditorProvider;
        this.teamPropertyEditorProvider = teamPropertyEditorProvider;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Recruit.class, recruitPropertyEditorProvider.get());
        binder.registerCustomEditor(Team.class, teamPropertyEditorProvider.get());
    }

    @GetMapping("/teambuilding/recruit/{id}")
    public RecruitResponse recruit(@PathVariable("id") Recruit recruit) {
        recruit.hit();
        recruitService.save(recruit);

        return recruit.toResponse(storageService.getUploadedImageUrl(recruit.getThumbnail()));
    }

    @GetMapping("/teambuilding/recruits")
    public List<RecruitShortResponse> recruits(
            @RequestParam(value = "teamName", required = false) String teamName,
            @RequestParam(value = "authorName", required = false) String authorName,
            @RequestParam(value = "desiredPos", required = false) String desiredPos
    ) throws InterruptedException {
        Thread.sleep(1000*1);

        List<Recruit> list = null;

        if (teamName != null) {
            list = recruitService.findAllByTeamName(teamName);
        } else if (authorName != null) {
            list = recruitService.findAllByAuthorName(authorName);
        } else if (desiredPos != null) {
            list = recruitService.findAllByDesiredPos(desiredPos);
        } else {
            list = recruitService.findAll();
        }

        return convertToShortResponse(list);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/teambuilding/recruit")
    public void registerRecruit(@ModelAttribute RecruitRequest recruitRequest, @AuthenticationPrincipal CustomOAuth2User me) throws IOException {
        Member author = memberService.findById(me.getId());
        Team recruitingTeam = teamService.findById(Long.parseLong(recruitRequest.teamId()));

        Recruit newRecruit = recruitRequest.toEntity(author, recruitingTeam);

        if (recruitRequest.isUploadedWithThumbnail()) storageService.store(recruitRequest.thumbnail());

        recruitService.save(newRecruit);
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

    @GetMapping("/myteam")
    public List<TeamShortResponse> myTeams(@AuthenticationPrincipal CustomOAuth2User me) {
        Member memberMe = memberService.findById(me.getId());

        return memberMe.getMemberTeams().stream().map(MemberTeam::toTeamShortResponse).toList();
    }

    @GetMapping("/check/myresume")
    public boolean isMyResumeExists(@AuthenticationPrincipal CustomOAuth2User me) {
        Member meMember = memberService.findById(me.getId());
        List<Resume> myResumeList = meMember.getResumes();

        return !myResumeList.isEmpty();
    }

    @GetMapping("/check/myteam/{teamId}")
    public boolean isMyTeam(@PathVariable("teamId") Team recruitingTeam, @AuthenticationPrincipal CustomOAuth2User me) {
        return recruitingTeam.isMemberByMemberId(me.getId());
    }

    @GetMapping("/check/application/status/team/{teamId}")
    public boolean isMyApplicationApplied(@PathVariable("teamId") Team recruitingTeam, @AuthenticationPrincipal CustomOAuth2User me) {
        return recruitingTeam.getApplications().stream().anyMatch(apply -> apply.isAlreadyApplied(me.getId()));
    }

    private List<RecruitShortResponse> convertToShortResponse(List<Recruit> list) {
        return list.stream().map(recruit -> recruit.toShortResponse(storageService.getUploadedImageUrl(recruit.getThumbnail()))).toList();
    }
}
