package com.app.api.access.facade;

import com.app.api.access.dto.request.LoginRequest;
import com.app.api.access.dto.request.SignUpRequest;
import com.app.api.access.dto.response.AccessTokenResponse;
import com.app.api.access.dto.response.LoginResponse;
import com.app.api.access.validator.PasswordValidator;
import com.app.domain.member.constant.MemberType;
import com.app.domain.member.constant.Role;
import com.app.domain.member.entity.Member;
import com.app.domain.member.service.MemberService;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.AuthenticationException;
import com.app.global.jwt.constant.GrantType;
import com.app.global.jwt.constant.TokenType;
import com.app.global.jwt.dto.JwtTokenDto;
import com.app.global.jwt.service.TokenManager;
import com.app.oauth.model.OAuthAttributes;
import com.app.oauth.service.SocialLoginApiService;
import com.app.oauth.service.SocialLoginApiServiceFactory;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccessFacade {

    private final MemberService memberService;
    private final TokenManager tokenManager;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public LoginResponse register(SignUpRequest signUpRequest) {
        PasswordValidator.passwordCheck(signUpRequest.getPassword(), signUpRequest.getPasswordCheck());

        Member member = signUpRequest.toMemberEntity(passwordEncoder, MemberType.LOCAL, Role.USER);
        member = memberService.registerMember(member);

        JwtTokenDto jwtTokenDto = tokenManager.createJwtTokenDto(member.getId(), member.getRole());
        member.updateRefreshToken(jwtTokenDto);

        return LoginResponse.of(jwtTokenDto);
    }

    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        loginRequest.setPassword(passwordEncoder.encode(loginRequest.getPassword()));

        Member member = memberService.findMemberByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
        JwtTokenDto jwtTokenDto = tokenManager.createJwtTokenDto(member.getId(), member.getRole());
        member.updateRefreshToken(jwtTokenDto);

        return LoginResponse.of(jwtTokenDto);
    }

    @Transactional
    public LoginResponse oauthLogin(String accessToken, MemberType memberType) {
        SocialLoginApiService socialLoginApiService = SocialLoginApiServiceFactory.getSocialLoginApiService(memberType);
        OAuthAttributes userInfo = socialLoginApiService.getUserInfo(accessToken);

        JwtTokenDto jwtTokenDto;
        Optional<Member> optionalMember = memberService.findMemberByEmail(userInfo.getEmail());

        Member oauthMember;
        if (optionalMember.isEmpty()) { // 신규 회원 가입
            oauthMember = userInfo.toMemberEntity(memberType, Role.USER);
            oauthMember = memberService.registerMember(oauthMember);

        } else { // 기존 회원일 경우
            oauthMember = optionalMember.get();

        }
        // 토큰 생성
        jwtTokenDto = tokenManager.createJwtTokenDto(oauthMember.getId(), oauthMember.getRole());
        oauthMember.updateRefreshToken(jwtTokenDto);

        return LoginResponse.of(jwtTokenDto);
    }

    public void logout(String accessToken) {

        // 1. 토큰 검증
        tokenManager.validateToken(accessToken);

        // 2. 토큰 타입 확인
        Claims tokenClaims = tokenManager.getTokenClaims(accessToken);
        String tokenType = tokenClaims.getSubject();

        if (!TokenType.isAccessToken(tokenType)) {
            throw new AuthenticationException(ErrorCode.NOT_ACCESS_TOKEN_TYPE);
        }

        // 3. refresh token 만료 처리
        Long memberId = Long.valueOf((Integer) tokenClaims.get("memberId"));
        Member member = memberService.findMemberById(memberId);
        member.expireRefreshToken(LocalDateTime.now());
    }

    public AccessTokenResponse createAccessTokenByRefreshToken(String refreshToken) {
        Member member = memberService.findMemberByRefreshToken(refreshToken);

        Date accessTokenExpireTime = tokenManager.createAccessTokenExpireTime();
        String accessToken = tokenManager.createAccessToken(member.getId(), member.getRole(), accessTokenExpireTime);

        return AccessTokenResponse.builder()
                .grantType(GrantType.BEARER.getType())
                .accessToken(accessToken)
                .accessTokenExpireTime(accessTokenExpireTime)
                .build();
    }
}
