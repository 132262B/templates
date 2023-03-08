package com.app.api.member.dto.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ModifyInfoRequestTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @DisplayName("둘다 값이 잘 들어간 경우")
    @Test
    void dtoValidateCheck_1() {
        ModifyInfoRequest request = new ModifyInfoRequest();
        request.setUsername("홍길동");
        request.setProfile("https://domain.com/img_110x110.jpg");

        Set<ConstraintViolation<ModifyInfoRequest>> violations = validator.validate(request);
        assertEquals(0, violations.size());
    }

    @DisplayName("profile URL, null인 경우")
    @Test
    void dtoValidateCheck_2() {
        ModifyInfoRequest request = new ModifyInfoRequest();
        request.setUsername("홍길동");
        request.setProfile(null);

        Set<ConstraintViolation<ModifyInfoRequest>> violations = validator.validate(request);
        assertEquals(0, violations.size());
    }

    @DisplayName("profile URL, 옳바르지 않는 URL인 경우")
    @Test
    void dtoValidateCheck_3() {
        ModifyInfoRequest request = new ModifyInfoRequest();
        request.setUsername("홍길동");
        request.setProfile("no_url");

        Set<ConstraintViolation<ModifyInfoRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());

        ConstraintViolation<ModifyInfoRequest> violation = violations.iterator().next();
        assertEquals("올바른 URL 형식이 아닙니다.", violation.getMessage());
    }

}