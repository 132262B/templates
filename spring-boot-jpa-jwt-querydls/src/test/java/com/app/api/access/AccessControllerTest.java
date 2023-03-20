package com.app.api.access;

import com.app.api.access.dto.request.OauthLoginRequest;
import com.app.domain.member.constant.MemberType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class AccessControllerTest extends AccessSteps {


    @Test
    void 카카오_OAUTH_계정생성_및_로그인() throws Exception {

        // given
        OauthLoginRequest oauthLoginRequestDto =new  OauthLoginRequest();
        oauthLoginRequestDto.setMemberType(MemberType.KAKAO.name());

        // when
        MockHttpServletResponse response = 회원가입(oauthLoginRequestDto);

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK);



    }



}