package com.example.hotsix.controller.test;

import com.example.hotsix.vo.MemberVo;
import com.example.hotsix.dto.MemberDto;
import com.example.hotsix.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.example.hotsix.service.test.TestService;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @GetMapping("/hot6man/hello")
    public String hello() {
        return "Hello, hot6Man!! - 07/25";
    }

    @GetMapping("/hot6man/hello2/member/{id}")
    public String hello2(@PathVariable long id) {
        return testService.getMember(id).getId().toString();
    }

    @GetMapping("/hot6man/hello3/member/{id}")
    public MemberDto hello3(@PathVariable long id) {
        Member member = testService.getMember(id);
        return MemberDto.fromEntity(member);
    }

//    @GetMapping("/hot6man/hello4/member/{id}")
//    public MemberVo hello4(@PathVariable long id) {
//        return testService.getPartialMemberInfoByProjections(id);
//    }
    @PostMapping("/hot6man/hello/member")
    public String addMember(@RequestBody MemberDto memberDto) {
        testService.addMember(memberDto);
        return "add Complete!";
    }
}
