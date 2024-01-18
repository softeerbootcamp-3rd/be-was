package webserver;

import controller.UserController;
import model.HttpStatus;
import model.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import service.UserService;

import java.io.DataOutputStream;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserHandlerTest {

    private static final String HOME = "/index.html";

    private DataOutputStream dataOutputStream;
    private final UserController userController = new UserController(new UserService());
    private final UserHandler userHandler = new UserHandler(userController);

    @ParameterizedTest
    @DisplayName("쿼리문이 제대로 전달되지 않은 경우 BadRequest를 전달한다.")
    @ValueSource(strings = {"invalidQuery"})
    public void testBadRequest(String query) {

        Response response = userHandler.handleUserCreation(query, dataOutputStream);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
    }

    @Test
    @DisplayName("사용자 생성에 성공한다.")
    public void testUserCreateSuccess() {
        String validQuery = "?userId=1111&password=p@ssw0rld&name=ddrongy&email=ddrongy@example.com";

        Response response = userHandler.handleUserCreation(validQuery, dataOutputStream);

        assertEquals(HttpStatus.REDIRECT, response.getStatus());
        assertEquals(HOME, response.getRedirectUrl());
    }

    @Test
    @DisplayName("중복된 ID인 경우 form_failed로 redirect 된다.")
    public void duplicateUser() {
        String validQuery = "?userId=2222&password=pass&name=John&email=john@example.com";
        Response response = userHandler.handleUserCreation(validQuery, dataOutputStream);
        Response responseDuplicate = userHandler.handleUserCreation(validQuery, dataOutputStream);

        assertEquals(HttpStatus.REDIRECT, responseDuplicate.getStatus());
        assertEquals("/user/form_failed.html", responseDuplicate.getRedirectUrl());
    }

    @ParameterizedTest
    @DisplayName("dto에 전달되는 값이 하나라도 빠지면 form_failed로 redirect된다.")
    @ValueSource(strings = {"?userId=3333&name=John&email=john@example.com",
            "?userId=3333&password=pass&name=John",
            "?userId=3333"})
    public void testInvalidQuery(String query) {
        Response response = userHandler.handleUserCreation(query, dataOutputStream);

        assertEquals(HttpStatus.REDIRECT, response.getStatus());
        assertEquals("/user/form_failed.html", response.getRedirectUrl());
    }
}
