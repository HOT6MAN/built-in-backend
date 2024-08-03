package com.example.hotsix.controller.recruit;

import com.example.hotsix.dto.recruit.RecruitResponse;
import com.example.hotsix.editor.RecruitPropertyEditor;
import com.example.hotsix.model.Recruit;
import com.example.hotsix.service.recruit.RecruitService;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Provider;

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
}
