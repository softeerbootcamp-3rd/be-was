package common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;

import static common.util.Util.*;
import static org.assertj.core.api.Assertions.assertThat;

class UtilTest {
    
    @Test
    @DisplayName("이진데이터의 디코딩 테스트")
    void decodeTest() throws UnsupportedEncodingException {
        
        //given
        String name = "%EB%B0%95%EC%9E%AC%EC%84%B1";

        //when
        String decodeName = decode(name);

        //then
        assertThat(decodeName).isEqualTo("박재성");
    }
    
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
