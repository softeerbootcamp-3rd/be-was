import controller.BasicController;
import controller.QNAController;
import db.Database;
import db.QnaRepository;
import http.Request;
import http.Response;
import http.SessionManager;
import model.User;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class QnaTest {
    private static final Logger logger = LoggerFactory.getLogger(UserTest.class);

    RequestHandlerAdapter adapter = new RequestHandlerAdapter();
    ViewResolver viewResolver = new ViewResolver();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);

    Database database = Database.getInstance();
    @Test
    @DisplayName("글쓰기")
    public void QnaCreate() throws IOException {
        //given
        QnaRepository qnaRepository = QnaRepository.getInstance();
        Response res = new Response(dos);

        User user = new User();
        user.setUserId("jomulagy");
        SessionManager.createSession(user,res,"SID");

        String cookieString = res.getCookieString();
        Request req = new Request("POST","/qna/form",cookieString,"title=dd&contents=cc");
        BasicController handler = new QNAController();

        //when

        ModelAndView mv = adapter.handle(req, res, handler);
        //then
        assertNotNull(qnaRepository.findQnaById(1L), "Qna 객체가 null이 아닌지 확인");
        assertEquals(qnaRepository.findQnaById(1L).getWriter(),user);
    }

    
}
