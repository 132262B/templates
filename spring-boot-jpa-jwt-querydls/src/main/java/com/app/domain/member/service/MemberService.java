package com.app.domain.member.service;

import com.app.api.member.dto.request.ModifyInfoRequest;
import com.app.domain.member.constant.MemberType;
import com.app.domain.member.entity.Member;
import com.app.domain.member.exception.DuplicateMemberException;
import com.app.domain.member.repository.MemberRepository;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.AuthenticationException;
import com.app.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member registerMember(Member member) {
        final String email = member.getEmail();
        final MemberType memberType = member.getMemberType();

        validateDuplicateMember(email, memberType);
        return memberRepository.save(member);
    }

    // 이메일 체크
    private void validateDuplicateMember(String email, MemberType memberType) {
        Optional<Member> optionalMember = memberRepository.findByEmailAndMemberType(email, memberType);
        if (optionalMember.isPresent()) {
            throw new DuplicateMemberException();
        }
    }

    public Optional<Member> findMemberByEmail(String email, MemberType memberType) {
        return memberRepository.findByEmailAndMemberType(email, memberType);
    }

    public Member findMemberByRefreshToken(String refreshToken) {
        Member member = memberRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new AuthenticationException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        LocalDateTime tokenExpirationTime = member.getTokenExpirationTime();
        if (tokenExpirationTime.isBefore(LocalDateTime.now())) {
            throw new AuthenticationException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }
        return member;
    }

    public Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_EXISTS));
    }

    @Transactional
    public void modifyMemberInfo(Member member, ModifyInfoRequest request) {
        member.changeMemberInfo(request.getUsername(), request.getProfile());
    }

    @Transactional
    public void secessionMember(Member member) {
        memberRepository.delete(member);
    }

    public Member findMemberByEmailAndPasswordAndMemberType(String email, String password, MemberType memberType) {
        return memberRepository.findMemberByEmailAndPasswordAndMemberType(email, password, memberType)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.EMAIL_OR_PASSWORD_INCONSISTENCY));
    }
}
