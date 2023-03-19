package com.app.api.access.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SignUpRequest {

    @Schema(description = "이메일", example = "member@domain.co.kr", required = true)
    @Email
    @NotBlank
    private String email;

    @Schema(description = "비밀번호", example = "!Password123!", required = true)
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$\n", message = "비밀번호는 대문자, 소문자, 숫자, 특수문자 중 하나 이상을 포함해야 합니다.")
    @Length(min = 8)
    @NotBlank
    private String password;

    @Schema(description = "비밀번호 확인", example = "!Password123!", required = true)
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$\n", message = "비밀번호는 대문자, 소문자, 숫자, 특수문자 중 하나 이상을 포함해야 합니다.")
    @Length(min = 8)
    @NotBlank
    private String passwordCheck;

    @Schema(description = "회원 이름", example = "홍길동", required = true)
    @NotBlank
    @Size(min = 2, max = 20)
    private String username;

    @Pattern(regexp = "^(https?://)?([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$", message = "올바른 URL 형식이 아닙니다.")
    @Schema(description = "프로필 이미지 경로", example = "https://domain.com/img_110x110.jpg")
    private String profile;
}
