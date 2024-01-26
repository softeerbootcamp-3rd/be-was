package common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static common.util.Util.*;
import static org.assertj.core.api.Assertions.assertThat;

class UtilTest {

    @Test
    @DisplayName("10자리 랜덤 문자열 생성")
    void getSessionIdTest() {

        //when
        String sessionId = getRandomString();

        //then
        assertThat(sessionId.length()).isEqualTo(10);
        assertThat(sessionId.getClass()).isEqualTo(String.class);
    }
}
