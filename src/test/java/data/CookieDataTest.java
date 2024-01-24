package data;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

public class CookieDataTest {
    @Test
    @DisplayName("CookieData 생성과 메서드 동작 검증")
    public void testCookieData() {
        // Given
        String sid = "sampleSid";
        int maxAge = 3600;
        CookieData cookieData = new CookieData(sid, maxAge);

        // When
        String cookieString = "sid=" + sid + "; Max-Age=" + maxAge + ";";

        // Then
        assertThat(cookieData.getSid()).isEqualTo(sid);
        assertThat(cookieData.getMaxAge()).isEqualTo(maxAge);
        assertThat(cookieData.toString()).isEqualTo(cookieString);
    }
}
