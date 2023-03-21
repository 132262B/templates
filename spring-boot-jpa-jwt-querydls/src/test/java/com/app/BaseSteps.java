package com.app;

import com.app.ApiTest;
import com.app.api.access.dto.request.SignUpRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class BaseSteps extends ApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    public MockHttpServletResponse 회원가입요청(SignUpRequest signUpRequest) throws Exception {
        return mockMvc.perform(
                        post("/api/sign-up")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(signUpRequest))
                )
                .andDo(print())
                .andReturn().getResponse();
    }

    public SignUpRequest 회원가입요청_생성() {
        final String email = "member@domain.co.kr";
        final String password = "!Password1234";
        final String profile = "https://domain.com/img_110x110.jpg";
        final String username = "회원";

        return SignUpRequest.builder()
                .email(email)
                .password(password)
                .passwordCheck(password)
                .profile(profile)
                .username(username)
                .build();
    }


}
