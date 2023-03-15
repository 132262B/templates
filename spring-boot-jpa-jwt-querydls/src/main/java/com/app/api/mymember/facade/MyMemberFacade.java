package com.app.api.mymember.facade;

import com.app.api.mymember.dto.request.ModifyInfoRequest;
import com.app.api.mymember.dto.response.MemberInfoResponse;
import com.app.domain.member.entity.Member;
import com.app.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyMemberFacade {

    private final MemberService memberService;


    public MemberInfoResponse findMemberInfo(Long memberId) {
        Member member = memberService.findMemberById(memberId);
        return MemberInfoResponse.of(member);
    }

    @Transactional
    public void modifyMemberInfo(Long memberId, ModifyInfoRequest request) {
        Member member = memberService.findMemberById(memberId);
        memberService.modifyMemberInfo(member, request);
    }

    @Transactional
    public void secessionMember(Long memberId) {
        Member member = memberService.findMemberById(memberId);
        memberService.secessionMember(member);
    }
}
