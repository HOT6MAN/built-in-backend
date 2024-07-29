package com.example.hotsix.service.member;

import com.example.hotsix.model.MemberImage;
import com.example.hotsix.repository.member.MemberImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberImageServiceImpl implements MemberImageService{
    private final MemberImageRepository memberImageRepository;


    @Override
    public MemberImage findMemberImageByMemberId(Long id) {
        return memberImageRepository.findImageByMemberId(id);
    }
}
