package com.example.hotsix.service.test;

import com.example.hotsix.dto.MemberDto;
import com.example.hotsix.vo.MemberVo;
import com.example.hotsix.enums.Process;
import com.example.hotsix.exception.BuiltInException;
import lombok.RequiredArgsConstructor;
import com.example.hotsix.model.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.hotsix.repository.test.TestRepository;

@Service
@RequiredArgsConstructor
public class TestService {
    private final TestRepository testRepository;

    @Transactional(readOnly = true)
    public Member getMember(Long id) {
        return testRepository.findById(id)
                .orElseThrow(() -> new BuiltInException(Process.USER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Member getMemberByQueryDsl(long id) {
        return testRepository.findMemberById(id);
    }

    @Transactional
    public void addMember(MemberDto memberDto) {
        Member member = Member.builder()
                .id(memberDto.getId())
                .build();
        testRepository.save(member);
    }

//    @Transactional(readOnly = true)
//    public MemberVo getPartialMemberInfoByProjections(long id) {
//        return testRepository.findPartialInfoById(id);
//    }
}
