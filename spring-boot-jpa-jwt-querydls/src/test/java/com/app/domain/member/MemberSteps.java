package com.app.domain.member;

import com.app.api.member.dto.request.ModifyInfoRequest;
import com.app.domain.member.constant.MemberType;
import com.app.domain.member.constant.Role;
import com.app.domain.member.entity.Member;

public class MemberSteps {

    public final static String USERNAME = "회원1";
    public final static String PROFILE = "https://domain.com/img_110x110.jpg";

    public static Member 회원정보_생성(String email) {
        return Member.builder()
                .email(email)
                .username(USERNAME)
                .profile(PROFILE)
                .role(Role.USER)
                .memberType(MemberType.KAKAO)
                .build();
    }

    public static ModifyInfoRequest 회원정보_수정(String username, String profile) {
        return new ModifyInfoRequest(username, profile);
    }
}
