package com.example.hotsix.repository.member;

import com.example.hotsix.model.MemberImage;

public interface MemberImageRepositoryCustom {
    MemberImage findImageByMemberId(Long id);

}
