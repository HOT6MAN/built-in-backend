package com.example.hotsix.controller;

import com.example.hotsix.vo.MemberVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.example.hotsix.service.MemberService;

@RestController
@RequiredArgsConstructor
public class HelloController {

    private final MemberService memberService;

    @GetMapping("/hot6man/hello")
    public String hello() {
        return "Hello, hot6Man!!";
    }

    @GetMapping("/hot6man/hello2/member/{id}")
    public String hello2(@PathVariable long id) {
        return memberService.getMember(id).getNickname();
    }

    @GetMapping("/hot6man/hello3/member/{id}")
    public String hello3(@PathVariable long id) {
        return memberService.getMemberByQueryDsl(id).getNickname();
    }

    @GetMapping("/hot6man/hello4/member/{id}")
    public MemberVo hello4(@PathVariable long id) {
        return memberService.getPartialMemberInfoByProjections(id);
    }
}
