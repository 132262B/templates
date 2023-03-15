package com.app.api.mymember.controller;

import com.app.api.mymember.dto.request.ModifyInfoRequest;
import com.app.api.mymember.dto.response.MemberInfoResponse;
import com.app.api.mymember.facade.MyMemberFacade;
import com.app.global.model.ApiResult;
import com.app.global.resolver.memberinfo.MemberInfo;
import com.app.global.resolver.memberinfo.MemberInfoDto;
import com.app.global.util.UriUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "my-member", description = "내정보 API")
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MyMemberController {

    private final MyMemberFacade myMemberFacade;

    @Tag(name = "my-member")
    @Operation(summary = "내 정보 조회 API", description = "내 정보 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "500", description = "서버 오류 발생(관리자 문의)"),
            @ApiResponse(responseCode = "M-003", description = "해당 회원은 존재하지 않는 회원입니다.")
    })
    @GetMapping
    public ResponseEntity<ApiResult<MemberInfoResponse>> findMyInfo(@MemberInfo MemberInfoDto memberInfoDto) {
        Long memberId = memberInfoDto.getMemberId();
        return ResponseEntity.ok(new ApiResult<>(myMemberFacade.findMemberInfo(memberId)));
    }

    @Tag(name = "my-member")
    @Operation(summary = "내 정보 변경 API", description = "내 정보 변경 API")
    @PutMapping
    public ResponseEntity<Void> modify(@Validated @RequestBody ModifyInfoRequest request,
                                        @MemberInfo MemberInfoDto memberInfoDto) {

        Long memberId = memberInfoDto.getMemberId();
        myMemberFacade.modifyMemberInfo(memberId, request);

        return ResponseEntity.created(UriUtil.createUri()).build();
    }

    @Tag(name = "my-member")
    @Operation(summary = "회원 탈퇴 API", description = "회원 탈퇴 API")
    @DeleteMapping
    public ResponseEntity<Void> secession(@MemberInfo MemberInfoDto memberInfoDto) {
        Long memberId = memberInfoDto.getMemberId();
        myMemberFacade.secessionMember(memberId);

        return ResponseEntity.noContent().build();
    }

}
