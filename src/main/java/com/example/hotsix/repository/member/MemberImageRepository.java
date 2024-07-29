package com.example.hotsix.repository.member;

import com.example.hotsix.model.MemberImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberImageRepository extends JpaRepository<MemberImage, Long>, MemberImageRepositoryCustom {
}
