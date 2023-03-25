package com.app.domain.member.repository;

import com.app.domain.member.constant.MemberType;
import com.app.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmailAndMemberType(String email, MemberType memberType);

    Optional<Member> findByRefreshToken(String refreshToken);

    Optional<Member> findMemberByEmailAndPasswordAndMemberType(String email, String password, MemberType memberType);
}
