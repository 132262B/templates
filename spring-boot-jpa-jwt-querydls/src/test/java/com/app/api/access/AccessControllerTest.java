package com.app.api.access;

import com.app.ApiTest;
import com.app.api.access.dto.OauthLoginDto;
import com.app.global.resolver.token.TokenDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class AccessControllerTest extends AccessSteps {

    @Test
    void 카카오_OAUTH_계정생성_및_로그인() throws Exception {

        // given
        OauthLoginDto.Request oauthLoginRequestDto = OauthLoginDto.Request.builder()
                .memberType("KAKAO")
                .build();

        // when
        MockHttpServletResponse response = 회원가입(oauthLoginRequestDto);

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK);



    }



}