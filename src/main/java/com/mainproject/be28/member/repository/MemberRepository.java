package com.mainproject.be28.member.repository;

import com.mainproject.be28.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Component

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
    Optional<Member> findByName(String name);
    boolean existsByEmail(String email);
    Optional<Member> findMemberByEmail(String email);

}
