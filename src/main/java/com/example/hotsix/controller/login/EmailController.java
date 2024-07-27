package com.example.hotsix.controller.login;

import com.example.hotsix.model.Member;
import com.example.hotsix.repository.member.MemberRepository;
import com.example.hotsix.service.login.MailLinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class EmailController {

    private final MailLinkService mailLinkService;
    private final MemberRepository memberRepository;

    @PostMapping("/email-link")
    public String emailLink(@RequestBody String email) {
        log.info(email);
        Member exist = memberRepository.findByEmail(email);

        String link = null;
        //이미 존재하는 이메일
        if(exist != null) {
            link = mailLinkService.createLink("email-login", email);
        }else{
            link = mailLinkService.createLink("register", email);
        }

        return link;
    }


    @PostMapping("/email-login")
    public void emailLogin(@RequestBody String email){

    }

    @PostMapping("/register")
    public void register(@RequestBody String email){

    }


}
