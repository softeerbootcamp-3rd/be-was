import controller.TemplateController;
import http.HttpStatus;
import http.Request;
import http.Response;
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

public class TemplateTest {

    MyHandlerAdapter adapter = new MyHandlerAdapter();
    ViewResolver viewResolver = new ViewResolver();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    public TemplateTest() throws IOException {
    }

    @Test
    @DisplayName("존재하는 html 찾기")
    public void FindExistsTemplate() throws IOException {
        //given
        Request req = new Request("GET","/index.html");
        Response res = new Response();
        Object handler = new TemplateController();

        //when
        ModelAndView mv = adapter.handle(req, res, handler);
        MyView view = viewResolver.resolve(mv.getViewName());
        view.render(dos, req, res);
        //then
        assertEquals(HttpStatus.OK,res.getStatus());
        assertEquals("/index.html",view.getViewPath());
    }

    @Test
    @DisplayName("존재하지않는 html 찾기")
    public void FindNonExistsTemplate() throws IOException {
        //given
        Request req = new Request("GET","/index1.html");
        Response res = new Response();
        Object handler = new TemplateController();

        //when
        ModelAndView mv = adapter.handle(req, res, handler);
        MyView view = viewResolver.resolve(mv.getViewName());
        view.render(dos, req, res);
        //then
        assertEquals(HttpStatus.NOT_FOUND,res.getStatus());
    }
}
