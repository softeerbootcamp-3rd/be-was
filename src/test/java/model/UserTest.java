package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserTest {
    @DisplayName("유저 검증 테스트")
    @Test
    void verifyUserTest() {
        String s = "sessionId=4db075ca-1c0c-4d4d-8c74-51a8d4015107";
        String[] arr = s.split("; ");
        System.out.println(arr.length);
        System.out.println(arr[0]);
    }
}
