package com.app.api.access;

import com.app.ApiTest;
import com.app.api.access.dto.OauthLoginDto;
import com.app.domain.member.MemberSteps;
import com.app.domain.member.constant.MemberType;
import com.app.global.resolver.token.TokenDto;
import com.app.oauth.model.OAuthAttributes;
import com.app.oauth.service.SocialLoginApiService;
import com.app.oauth.service.SocialLoginApiServiceFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class AccessSteps extends ApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private SocialLoginApiService socialLoginApiService;

    @Autowired
    private SocialLoginApiServiceFactory socialLoginApiServiceFactory;

    public MockHttpServletResponse 회원가입(OauthLoginDto.Request oauthLoginRequestDto) throws Exception {

        final String token = "Bearer tokentoken";

        OAuthAttributes userInfo = OAuthAttributes.builder()
                .email("member@domain.com")
                .name(MemberSteps.USERNAME)
                .profile(MemberSteps.PROFILE)
                .memberType(MemberType.KAKAO)
                .build();

        Mockito.when(socialLoginApiService.getUserInfo(token)).thenReturn(userInfo);

        return mockMvc.perform(
                        post("/api/oauth/login")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .header(HttpHeaders.AUTHORIZATION, token)
                                .content(objectMapper.writeValueAsString(oauthLoginRequestDto))
                )
                .andDo(print())
                .andReturn().getResponse();
    }

}
