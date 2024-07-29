package com.example.hotsix.service.member;

import com.example.hotsix.model.Member;
import org.springframework.core.io.Resource;

import java.nio.file.Path;

public interface MemberService {
    Member findMemberProfileByMemberId(Long id);
    boolean deleteMemberByMebmerId(Long id);

    boolean updateMemberProfileByMemberId(Member dto);
    boolean updateMemberProfileImageByMemberId(Member dto);
    Resource findMemberProfileImageByMemberId(Long id);
    Resource getImagePath();
    Path getImagePath(Member member);
    String getContentType(Member member);
}
