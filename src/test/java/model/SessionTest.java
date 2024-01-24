package model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SessionTest {
    @Test
    @DisplayName("Session 생성자로 올바른 인스턴스가 생성되는 지 테스트")
    public void createSessionTest() {
        // when
        Session session = new Session("userId");

        // then
        Assertions.assertThat(session.getUserId()).isEqualTo("userId");
        Assertions.assertThat(session.getExpireDate()).isEqualTo(session.getCreateDate().plusHours(24));
        System.out.println(session);
    }
}
