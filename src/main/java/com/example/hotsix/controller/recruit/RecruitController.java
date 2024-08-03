package com.example.hotsix.controller.recruit;

import com.example.hotsix.dto.recruit.RecruitResponse;
import com.example.hotsix.dto.recruit.RecruitShortResponse;
import com.example.hotsix.editor.RecruitPropertyEditor;
import com.example.hotsix.model.Recruit;
import com.example.hotsix.service.recruit.RecruitService;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Provider;

import java.util.List;

@RestController
public class RecruitController {

    private final RecruitService recruitService;
    private final Provider<RecruitPropertyEditor> recruitPropertyEditorProvider;

    public RecruitController(RecruitService recruitService,
                             Provider<RecruitPropertyEditor> recruitPropertyEditorProvider) {
        this.recruitService = recruitService;
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
}
