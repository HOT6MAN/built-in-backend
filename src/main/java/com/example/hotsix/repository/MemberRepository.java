package com.example.hotsix.repository;

import com.example.hotsix.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    Member findById(long id);
    Member findByEmail(String email);
}
