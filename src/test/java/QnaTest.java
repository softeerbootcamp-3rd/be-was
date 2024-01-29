import controller.BasicController;
import controller.MainController;
import controller.QNAController;
import controller.UserController;
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
    private static final Logger logger = LoggerFactory.getLogger(QnaTest.class);
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

        user = new User();
        user.setUserId("jomulagy");
        user.setName("김지훈");
        user.setEmail("jomulagy@gmail.com");
        SessionManager.createSession(user,res,"SID");

        cookieString = res.getCookieString();

    }

    @Test
    @DisplayName("글쓰기")
    public void QnaCreate() throws IOException {
        //given

        Request req = new Request("POST","/qna/form",cookieString,"title=dd&contents=dddd%0D%0Adddd");
        logger.debug("req.getBody() = {}",req.getBody().get("contents"));
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
        Qna qna1 = new Qna();
        qna1.setWriter(user);
        qna1.setTitle("dd");
        qna1.setContent("dddd\ndddd");
        qnaRepository.addQna(qna1);

        Qna qna2 = new Qna();
        qna2.setWriter(user);
        qna2.setTitle("dd");
        qna2.setContent("cc");
        qnaRepository.addQna(qna2);
        //when

        ModelAndView mv = adapter.handle(req, res, handler);
        String posts = (String) mv.getModel().getAttribute("posts");


        //then
        assertNotNull(posts);
        logger.debug(posts);

    }

    @Test
    @DisplayName("글 상세 데이터")
    public void QnaDetail() throws IOException {

        Request req = new Request("GET","/qna/detail?id=1",cookieString,"");
        BasicController handler = new QNAController();
        Qna qna1 = new Qna();
        qna1.setWriter(user);
        qna1.setTitle("dd");
        qna1.setContent("cc\ncc");
        qnaRepository.addQna(qna1);

        //when

        ModelAndView mv = adapter.handle(req, res, handler);
        String post = (String) mv.getModel().getAttribute("post");

        //then
        assertNotNull(post);
        logger.debug(post);

    }

    @Test
    @DisplayName("작성자 정보 확인하기")
    public void QnaAuthor() throws IOException {

        Request req = new Request("GET","/user/profile?postId=1",cookieString,"");
        BasicController handler = new UserController();
        Qna qna1 = new Qna();
        qna1.setWriter(user);
        qna1.setTitle("dd");
        qna1.setContent("cc\ncc");
        qnaRepository.addQna(qna1);

        //when

        ModelAndView mv = adapter.handle(req, res, handler);
        String author = (String) mv.getModel().getAttribute("author");

        //then
        assertNotNull(author);
        logger.debug(author);
    }
}
