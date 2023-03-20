package com.app.domain.member.service;

import com.app.api.member.dto.request.ModifyInfoRequest;
import com.app.domain.member.entity.Member;
import com.app.domain.member.exception.DuplicateMemberException;
import com.app.domain.member.repository.MemberRepository;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.AuthenticationException;
import com.app.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member registerMember(Member member) {
        validateDuplicateMember(member.getEmail());
        return memberRepository.save(member);
    }

    // 이메일 체크
    private void validateDuplicateMember(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isPresent()) {
            throw new DuplicateMemberException();
        }
    }

    public Optional<Member> findMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
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

    public Member findMemberByEmailAndPassword(String email, String password) {
        return memberRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.EMAIL_OR_PASSWORD_INCONSISTENCY));
    }
}
