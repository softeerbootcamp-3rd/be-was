package service;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MemberJoinServiceTest {
    @Test
    public void createUserTest() {
        Map<String, String> params = new HashMap<>();
        params.put("userId", "id");
        params.put("password", "pw");
        params.put("name", "kim");
        params.put("email", "123@naver.com");

        MemberJoinService memberJoinService = new MemberJoinService();
        memberJoinService.createUser(params);

        assertThat(memberJoinService.getUserList().size()).isEqualTo(1);
    }

}