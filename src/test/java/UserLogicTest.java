import Controller.User.UserLogic.UserLogic;
import HTTPModel.HttpStatus;
import HTTPModel.Request;
import HTTPModel.Response;
import db.Database;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserLogicTest {

    @BeforeEach
    void init(){
        User user = new User("1","1234","zensen6","a@naver.com");
        Database.addUser(user);
    }

    @Test
    @DisplayName("User Logic의 회원가입의 중복 회원아이디를 제대로 처리하는지 확인한다.")
    void signupTest(){
        Request request = new Request();
        String testBody = "userId=1&password=1234&name=zensen6&email=a%40naver.com";
        request.SetBody(testBody);

        UserLogic.userSignup(request, null);
        assertThat(Database.findAll().size() == 1);
    }


    @Test
    @DisplayName("User Logic의 회원가입의 회원아이디를 제대로 저장하는지 확인한다.")
    void signupTest2(){
        Request request = new Request();
        String testBody = "userId=2&password=1234&name=zensen6&email=a%40naver.com";
        request.SetBody(testBody);

        UserLogic.userSignup(request, null);
        assertEquals(Database.findAll().size(), 2);
    }


    @Test
    @DisplayName("USer Logic의 로그인이 제대로 되는지 확인한다.")
    void loginTest(){
        Request request = new Request();
        String testBody = "userId=1&password=1234&name=zensen6&email=a%40naver.com";
        request.SetBody(testBody);

        Response response = UserLogic.userLoginSuccess(request, null);
        assertEquals(HttpStatus.REDIRECT, response.getHttpStatus());
        assertThat(response.getRedirectUrl()).isEqualTo("/index.html");

    }


}
