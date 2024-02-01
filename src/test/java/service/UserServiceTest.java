package service;

import exception.SourceException;
import util.ParseParams;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserServiceTest {
    private final UserService userService = new UserService();

    @Test
    @DisplayName("사용자 생성 성공 테스트")
    public void createUserTest() {
        // given
        ParseParams parseParams = ParseParams.from("userId=javajigi&password=password&name=jaesung&email=javajigi@slipp.net");

        // when
        User user = userService.createUser(parseParams);

        // then
        Assertions.assertEquals("jaesung", user.getName());
        Assertions.assertEquals("javajigi@slipp.net", user.getEmail());
    }

    @Test
    @DisplayName("중복 사용자 저장 예외 테스트")
    public void duplicatedUserExceptionTest() {
        // given
        ParseParams firstParseParams = ParseParams.from("userId=win9&password=password&name=seunggu&email=win9@gmail.com");

        // when
        userService.createUser(firstParseParams);

        // then
        Assertions.assertThrows(SourceException.class, () -> userService.createUser(firstParseParams));
    }
}
