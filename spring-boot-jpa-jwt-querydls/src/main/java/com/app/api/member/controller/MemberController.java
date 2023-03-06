package com.app.api.member.controller;

import com.app.api.member.dto.response.MemberInfoResponse;
import com.app.api.member.facade.MemberFacade;
import com.app.global.resolver.memberinfo.MemberInfo;
import com.app.global.resolver.memberinfo.MemberInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "member", description = "회원 API")
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberFacade memberFacade;

    @Tag(name = "member")
    @Operation(summary = "내 정보 조회 API", description = "내 정보 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "500", description = "서버 오류 발생(관리자 문의)"),
            @ApiResponse(responseCode = "M-003", description = "해당 회원은 존재하지 않는 회원입니다.")
    })
    @GetMapping
    public ResponseEntity<MemberInfoResponse> findMyInfo(@MemberInfo MemberInfoDto memberInfoDto) {
        Long memberId = memberInfoDto.getMemberId();
        return ResponseEntity.ok(memberFacade.findMemberInfo(memberId));
    }

}
