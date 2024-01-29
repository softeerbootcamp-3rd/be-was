package service;

import db.Database;
import exception.CreateUserException;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.SessionManager;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    private final UserService userService = new UserService();

    @BeforeEach
    void beforeEach() {
        Database.clearAll();
    }

    @Test
    void 회원가입성공() {
        //Given
        String userId = "gyeongsu";
        String password = "1234";
        String name = "gyeongsu";
        String email = "gyeongsu@hyundai.com";

        //When
        String id = userService.create(userId, password, name, email);

        //Then
        User user = Database.findUserById(id);

        assertEquals(userId, user.getUserId());
        assertEquals(password, user.getPassword());
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
    }

    @Test
    void 회원가입null값이들어왔을때() {
        //Given
        String userId = "gyeongsu";
        String password = "1234";
        String name = "gyeongsu";
        String email = null;

        //When
        CreateUserException e = assertThrows(CreateUserException.class,
                () -> userService.create(userId, password, name, email));


        //Then
        assertEquals(CreateUserException.NULL_EMAIL, e.getMessage());
    }

    @Test
    void 회원가입중복아이디() {
        //Given
        String userId = "gyeongsu";
        String password = "1234";
        String name = "gyeongsu";
        String email = "gyeongsu@hyundai.com";
        userService.create(userId, password, name, email);

        //When
        CreateUserException e = assertThrows(CreateUserException.class,
                () -> userService.create(userId, "5678", "호날두", "ronaldo@hyundai.com"));


        //Then
        assertEquals(CreateUserException.DUPLICATE_ID, e.getMessage());
    }

    @Test
    void 회원가입중복이메일() {
        //Given
        String userId = "gyeongsu";
        String password = "1234";
        String name = "gyeongsu";
        String email = "gyeongsu@hyundai.com";
        userService.create(userId, password, name, email);

        //When
        CreateUserException e = assertThrows(CreateUserException.class,
                () -> userService.create("ronaldo", "5678", "호날두", "gyeongsu@hyundai.com"));


        //Then
        assertEquals(CreateUserException.DUPLICATE_EMAIL, e.getMessage());
    }

    @Test
    void 로그인성공() {
        //Given
        String userId = "gyeongsu";
        String password = "1234";
        String name = "gyeongsu";
        String email = "gyeongsu@hyundai.com";
        userService.create(userId, password, name, email);

        //When
        String sessionId = userService.login(userId, password);

        //Then
        User user = SessionManager.getUserBySessionId(sessionId);

        assertEquals(userId, user.getUserId());
        assertEquals(password, user.getPassword());
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
    }

    @Test
    void 로그인존재하지않는아이디() {
        //Given

        //When
        String sessionId = userService.login("gyeongsu", "1234");

        //Then
        assertNull(sessionId);
    }

    @Test
    void 로그인비밀번호오류() {
        //Given
        String userId = "gyeongsu";
        String password = "1234";
        String name = "gyeongsu";
        String email = "gyeongsu@hyundai.com";
        userService.create(userId, password, name, email);

        //When
        String sessionId = userService.login("gyeongsu", "12345");

        //Then
        assertNull(sessionId);
    }

    @Test
    void 회원정보수정성공() {
        //Given
        String userId = "gyeongsu";
        String password = "1234";
        String name = "gyeongsu";
        String email = "gyeongsu@hyundai.com";
        userService.create(userId, password, name, email);

        String newPassword = "5678";
        String newName = "호날두";
        String newEmail = "ronaldo@hyundai.com";

        //When
        userService.update(userId, newPassword, newName, newEmail);

        //Then
        User user = Database.findUserById(userId);

        assertEquals(userId, user.getUserId());
        assertEquals(newPassword, user.getPassword());
        assertEquals(newName, user.getName());
        assertEquals(newEmail, user.getEmail());
    }

    @Test
    void 회원정보수정null값이들어왔을때() {
        //Given
        String userId = "gyeongsu";
        String password = "1234";
        String name = "gyeongsu";
        String email = "gyeongsu@hyundai.com";
        userService.create(userId, password, name, email);

        //When
        CreateUserException e = assertThrows(CreateUserException.class,
                () -> userService.update(userId, "5678", null, email));


        //Then
        assertEquals(CreateUserException.NULL_NAME, e.getMessage());
    }

    @Test
    void 회원정보수정중복이메일() {
        //Given
        userService.create("gyeongsu", "1234", "gyeongsu", "gyeongsu@hyundai.com");

        String userId = "ronaldo";
        String password = "1234";
        String name = "호날두";
        String email = "ronaldo@hyundai.com";
        userService.create(userId, password, name, email);

        //When
        CreateUserException e = assertThrows(CreateUserException.class,
                () -> userService.update(userId, password, "gyeongsu", "gyeongsu@hyundai.com"));


        //Then
        assertEquals(CreateUserException.DUPLICATE_EMAIL, e.getMessage());
    }
}
