package util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SessionUtilTest {

    @Test
    @DisplayName("length 자릿수 만큼의 랜덤한 숫자 문자열 생성")
    public void createSessionIdTest() {
        // given
        int lengthFive = 5;
        int lengthEight = 8;

        // when
        String sessionIdFive = SessionUtil.createSessionId(lengthFive);
        String sessionIdEight = SessionUtil.createSessionId(lengthEight);

        // then
        Assertions.assertThat(sessionIdFive.length()).isEqualTo(lengthFive);
        Assertions.assertThat(sessionIdEight.length()).isEqualTo(lengthEight);
    }
}
