import controller.UserController;
import db.Database;
import http.HttpStatus;
import http.Request;
import http.Response;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.ModelAndView;
import webserver.MyView;
import webserver.ViewResolver;
import webserver.adaptor.MyHandlerAdapter;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserTest {

    MyHandlerAdapter adapter = new MyHandlerAdapter();
    ViewResolver viewResolver = new ViewResolver();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);

    Database database = Database.getInstance();
    public UserTest() throws IOException {
    }

    @Test
    @DisplayName("회원가입 DB 테스트")
    public void UserCreate() throws IOException {
        //given
        Request req = new Request("GET","/user/create?userId=jomulagy&password=1234%21&name=%EA%B9%80%EC%A7%80%ED%9B%88&email=jomulagy688%40gmail.com");
        Response res = new Response();
        Object handler = new UserController();
        ModelAndView mv = adapter.handle(req, res, handler);
        //when

        database.findUserById("jomulagy");
        //then
        User foundUser = database.findUserById("jomulagy");
        assertNotNull(foundUser);
        assertEquals("jomulagy", foundUser.getUserId());
        assertEquals("김지훈", foundUser.getName());
        assertEquals("1234!", foundUser.getPassword());
    }

    @Test
    @DisplayName("회원가입 후 redirect")
    public void UserCreateRedirect() throws IOException {
        //given
        Request req = new Request("GET","/user/create?userId=jomulagy&password=1234%21&name=%EA%B9%80%EC%A7%80%ED%9B%88&email=jomulagy688%40gmail.com");
        Response res = new Response();
        Object handler = new UserController();
        ModelAndView mv = adapter.handle(req, res, handler);
        //when

        MyView view = viewResolver.resolve(mv.getViewName());
        view.render(dos, req, res);
        //then
        assertEquals(HttpStatus.MOVED_PERMANENTLY,res.getStatus());
        assertEquals("/user/login.html",res.getLocation());
    }
}
