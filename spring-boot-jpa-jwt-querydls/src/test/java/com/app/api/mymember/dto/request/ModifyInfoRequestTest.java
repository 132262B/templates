package com.app.api.mymember.dto.request;

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
        final String username = "홍길동";
        final String profile = "https://domain.com/img_110x110.jpg";
        ModifyInfoRequest request = new ModifyInfoRequest(username, profile);

        Set<ConstraintViolation<ModifyInfoRequest>> violations = validator.validate(request);
        assertEquals(0, violations.size());
    }

    @DisplayName("profile URL, null인 경우")
    @Test
    void dtoValidateCheck_2() {
        final String username = "홍길동";
        final String profile = null;
        ModifyInfoRequest request = new ModifyInfoRequest(username, profile);

        Set<ConstraintViolation<ModifyInfoRequest>> violations = validator.validate(request);
        assertEquals(0, violations.size());
    }

    @DisplayName("profile URL, 옳바르지 않는 URL인 경우")
    @Test
    void dtoValidateCheck_3() {
        final String username = "홍길동";
        final String profile = "no_url";
        ModifyInfoRequest request = new ModifyInfoRequest(username, profile);

        Set<ConstraintViolation<ModifyInfoRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());

        ConstraintViolation<ModifyInfoRequest> violation = violations.iterator().next();
        assertEquals("올바른 URL 형식이 아닙니다.", violation.getMessage());
    }

}