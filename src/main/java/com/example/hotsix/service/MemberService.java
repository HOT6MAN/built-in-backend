package com.example.hotsix.service;

import lombok.RequiredArgsConstructor;
import com.example.hotsix.model.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.hotsix.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Member getMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow();
    }

    @Transactional(readOnly = true)
    public Member getMemberByQueryDsl(long id) {
        return memberRepository.findMemberById(id);
    }
}
