import controller.BasicController;
import controller.MainController;
import controller.QNAController;
import db.Database;
import db.QnaRepository;
import http.Request;
import http.Response;
import http.SessionManager;
import model.Qna;
import model.User;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.ModelAndView;
import webserver.ViewResolver;
import webserver.adaptor.RequestHandlerAdapter;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class QnaTest {
    private static final Logger logger = LoggerFactory.getLogger(UserTest.class);
    Request req;
    Response res;
    String cookieString;
    QnaRepository qnaRepository;
    RequestHandlerAdapter adapter;
    DataOutputStream dos;
    User user;
    @BeforeEach
    public void setUp() {
        adapter = new RequestHandlerAdapter();
        ViewResolver viewResolver = new ViewResolver();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        dos = new DataOutputStream(baos);

        Database database = Database.getInstance();

        qnaRepository = QnaRepository.getInstance();
        Response res = new Response(dos);

        User user = new User();
        user.setUserId("jomulagy");
        SessionManager.createSession(user,res,"SID");

        cookieString = res.getCookieString();

    }

    @Test
    @DisplayName("글쓰기")
    public void QnaCreate() throws IOException {
        //given

        Request req = new Request("POST","/qna/form",cookieString,"title=dd&contents=cc");
        BasicController handler = new QNAController();

        //when

        ModelAndView mv = adapter.handle(req, res, handler);
        //then
        assertNotNull(qnaRepository.findQnaById(1L), "Qna 객체가 null이 아닌지 확인");
        assertEquals(qnaRepository.findQnaById(1L).getWriter(),user);
    }

    @Test
    @DisplayName("글 리스트 조회하기")
    public void QnaList() throws IOException {

        Request req = new Request("GET","/",cookieString,"");
        BasicController handler = new MainController();
        Qna qna = new Qna();
        qna.setWriter(user);
        qna.setTitle("dd");
        qna.setContent("cc");
        QnaRepository.addQna(qna);
        //when

        ModelAndView mv = adapter.handle(req, res, handler);
        Object postsAttribute = mv.getModel().getAttribute("posts");


        //then
        if (postsAttribute instanceof Collection) {
            Collection<Qna> collections = (Collection<Qna>) postsAttribute;
            List<Qna> posts = new ArrayList<Qna>(collections);
            assertNotNull(posts);
            assertEquals(posts.get(0),qna);

        }

    }


}
