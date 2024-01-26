package common;

import common.exception.EmptyFormException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


class InputValidatorTest {

    @Test
    @DisplayName("입력한 회원가입 정보 유효성 검증 성공 테스트")
    void validateUserInfoSuccess() {

        //given
        String queryString = "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net";

        //when, then
        assertDoesNotThrow(()
                -> InputValidator.validateUserInfo(queryString));
    }

    @Test
    @DisplayName("입력한 회원가입 정보 유효성 검증 실패 테스트")
    void validateUserInfoFail() {

        //given
        String noEmailQueryString = "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=";

        //when, then
        assertThatThrownBy(() -> InputValidator.validateUserInfo(noEmailQueryString))
                .isInstanceOf(EmptyFormException.class);
    }
}
