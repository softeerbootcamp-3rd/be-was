import controller.StaticController;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StaticTest {

    MyHandlerAdapter adapter = new MyHandlerAdapter();
    ViewResolver viewResolver = new ViewResolver();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    public StaticTest() throws IOException {
    }

    @Test
    @DisplayName("존재하는 css 찾기")
    public void FindExistsCss() throws IOException {
        //given
        Request req = new Request("GET","/css/styles.css");
        Response res = new Response();
        Object handler = new StaticController();

        //when
        ModelAndView mv = adapter.handle(req, res, handler);
        MyView view = viewResolver.resolve(mv.getViewName());
        view.render(dos, req, res);
        //then
        assertEquals(HttpStatus.OK,res.getStatus());
    }

    @Test
    @DisplayName("존재하지않는 css 찾기")
    public void FindNonExistsCss() throws IOException {
        //given
        Request req = new Request("GET","/css/styles1.css");
        Response res = new Response();
        Object handler = new StaticController();

        //when
        ModelAndView mv = adapter.handle(req, res, handler);
        MyView view = viewResolver.resolve(mv.getViewName());
        view.render(dos, req, res);
        //then
        assertEquals(HttpStatus.NOT_FOUND,res.getStatus());
    }

    @Test
    @DisplayName("존재하는 js 찾기")
    public void FindExistsJs() throws IOException {
        //given
        Request req = new Request("GET","/js/scripts.js");
        Response res = new Response();
        Object handler = new StaticController();

        //when
        ModelAndView mv = adapter.handle(req, res, handler);
        MyView view = viewResolver.resolve(mv.getViewName());
        view.render(dos, req, res);
        //then
        assertEquals(HttpStatus.OK,res.getStatus());
    }

    @Test
    @DisplayName("존재하지 않는 js 찾기")
    public void FindNonExistsJs() throws IOException {
        //given
        Request req = new Request("GET","/js/scripts1.js");
        Response res = new Response();
        Object handler = new StaticController();

        //when
        ModelAndView mv = adapter.handle(req, res, handler);
        MyView view = viewResolver.resolve(mv.getViewName());
        view.render(dos, req, res);
        //then
        assertEquals(HttpStatus.NOT_FOUND,res.getStatus());
    }
}
