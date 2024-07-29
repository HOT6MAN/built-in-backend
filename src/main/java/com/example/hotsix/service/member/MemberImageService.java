package com.example.hotsix.service.member;

import com.example.hotsix.model.MemberImage;

public interface MemberImageService {
    MemberImage findMemberImageByMemberId(Long id);
}
