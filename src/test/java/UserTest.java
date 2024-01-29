import controller.BasicController;
import controller.QNAController;
import controller.UserController;
import db.Database;
import db.QnaRepository;
import http.HttpStatus;
import http.Request;
import http.Response;
import http.SessionManager;
import model.Qna;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.ModelAndView;
import webserver.adaptor.RequestHandlerAdapter;
import webserver.ViewResolver;
import webserver.view.View;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserTest {
    private static final Logger logger = LoggerFactory.getLogger(UserTest.class);

    RequestHandlerAdapter adapter = new RequestHandlerAdapter();
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
        Request req = new Request("GET","/user/create?userId=jomulagy&password=1234%21&name=%EA%B9%80%EC%A7%80%ED%9B%88&email=jomulagy688%40gmail.com","","");
        Response res = new Response(dos);
        BasicController handler = new UserController();
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
        Request req = new Request("GET","/user/create?userId=jomulagy&password=1234%21&name=%EA%B9%80%EC%A7%80%ED%9B%88&email=jomulagy688%40gmail.com","","");
        Response res = new Response(dos);
        BasicController handler = new UserController();
        ModelAndView mv = adapter.handle(req, res, handler);
        //when

        View view = viewResolver.resolve(mv.getViewName());
        view.render(req, res,mv.getModel());
        //then
        assertEquals(HttpStatus.MOVED_PERMANENTLY,res.getStatus());
        assertEquals("/user/login.html",res.getLocation());
    }


}
