package com.app.api.access.controller;

import com.app.api.access.dto.OauthLoginDto;
import com.app.api.access.dto.response.AccessTokenResponse;
import com.app.api.access.facade.AccessFacade;
import com.app.api.access.validator.OauthValidator;
import com.app.domain.member.constant.MemberType;
import com.app.global.resolver.token.AuthorizationToken;
import com.app.global.resolver.token.TokenDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "authentication", description = "로그인/로그아웃/토큰재발급 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oauth")
public class AccessController {

    private final OauthValidator oauthValidator;

    private final AccessFacade accessFacade;

    @Tag(name = "authentication")
    @Operation(summary = "소셜 로그인 API", description = "소셜 로그인 API")
    @PostMapping("/login")
    public ResponseEntity<OauthLoginDto.Response> oauthLogin(@RequestBody OauthLoginDto.Request oauthLoginRequestDto,
                                                             @AuthorizationToken TokenDto token) {

        oauthValidator.validateMemberType(oauthLoginRequestDto.getMemberType());

        OauthLoginDto.Response jwtTokenResponseDto = accessFacade
                .oauthLogin(token.getToken(), MemberType.from(oauthLoginRequestDto.getMemberType()));
        return ResponseEntity.ok(jwtTokenResponseDto);
    }

    @Tag(name = "authentication")
    @Operation(summary = "로그아웃 API", description = "로그아웃시 refresh token 만료 처리")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthorizationToken TokenDto token) {
        accessFacade.logout(token.getToken());

        return ResponseEntity.noContent().build();
    }

    @Tag(name = "authentication")
    @Operation(summary = "Access Token 재발급 API", description = "Access Token 재발급 API")
    @PostMapping("/reissued/access-token")
    public ResponseEntity<AccessTokenResponse> createAccessToken(@AuthorizationToken TokenDto token) {
        return ResponseEntity.ok(accessFacade.createAccessTokenByRefreshToken(token.getToken()));
    }
}
