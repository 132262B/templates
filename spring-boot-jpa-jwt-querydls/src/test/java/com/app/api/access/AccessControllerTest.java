package com.app.api.access;

import com.app.BaseSteps;
import com.app.api.access.dto.request.SignUpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("로그인,회원가입 테스트")
class AccessControllerTest extends BaseSteps {

    @Test
    @DisplayName("회원가입이 성공하면 상태코드는 200을 반환.")
    void ifYouSuccessfullySignUpReturnsStatusCode200() throws Exception {

        // given
        SignUpRequest request = 회원가입요청_생성();

        // when
        MockHttpServletResponse response = 회원가입요청(request);

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("이메일이 중복된 경우, 상태코드 400 반환.")
    void duplicateEmailStatusCode400() throws Exception {

        // given
        SignUpRequest request = 회원가입요청_생성();

        // when
        회원가입요청(request);
        MockHttpServletResponse response = 회원가입요청(request);

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }



}