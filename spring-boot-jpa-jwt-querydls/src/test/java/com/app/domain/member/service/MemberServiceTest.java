package com.app.domain.member.service;

import com.app.domain.member.MemberSteps;
import com.app.domain.member.constant.MemberType;
import com.app.domain.member.constant.Role;
import com.app.domain.member.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static com.app.domain.member.MemberSteps.PROFILE;
import static com.app.domain.member.MemberSteps.USERNAME;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    void 회원생성_성공() {
        // given
        final Long memberId = 1L;
        final String email = "member1@domain.com";
        Member member = MemberSteps.회원정보_생성(email);

        // when
        memberService.registerMember(member);
        final Member memberResponse = memberService.findMemberById(memberId);

        // then
        assertThat(memberResponse.getEmail()).isEqualTo(email);
        assertThat(memberResponse.getUsername()).isEqualTo(USERNAME);
        assertThat(memberResponse.getProfile()).isEqualTo(PROFILE);
        assertThat(memberResponse.getRole()).isEqualTo(Role.USER);
        assertThat(memberResponse.getMemberType()).isEqualTo(MemberType.KAKAO);
    }

}