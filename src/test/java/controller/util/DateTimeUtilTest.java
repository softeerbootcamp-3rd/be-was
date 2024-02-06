package controller.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static controller.util.DateTimeUtil.*;

class DateTimeUtilTest {

    @Test
    @DisplayName("LocalDateTime 객체를 헤더에 추가할 문자열로 변환하는 메서드 테스트")
    void getGMTDateStringTest() {
        LocalDateTime localDate = LocalDateTime.of(2024, 1, 20, 19, 30);
        assertThat(getGMTDateString(localDate))
                .isEqualTo("Sat, 20 Jan 2024 19:30:00 GMT");
    }

    @Test
    @DisplayName("헤더에 날짜 문자열을 LocalDateTime 객체로 변환하는 메서드 테스트")
    void parseGMTDateStringTest() {
        parseGMTDateString("Thu, 18 Jan 2024 15:44:23 GMT")
                .isEqual(LocalDateTime.of(2024, 1, 18, 15, 44, 23));
    }
}
