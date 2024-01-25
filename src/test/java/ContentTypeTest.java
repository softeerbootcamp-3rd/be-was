import controller.BasicController;
import http.Request;
import http.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.ModelAndView;
import webserver.view.InternalResourceView;
import webserver.ViewResolver;
import webserver.adaptor.MyHandlerAdapter;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContentTypeTest {
    MyHandlerAdapter adapter = new MyHandlerAdapter();
    ViewResolver viewResolver = new ViewResolver();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    public ContentTypeTest() throws IOException {
    }
    @Test
    @DisplayName("html")
    public void HtmlContentType() throws IOException {
        //given
        Request req = new Request("GET","/css/styles.css");
        Response res = new Response();
        BasicController handler = new ResourceController();

        //when
        ModelAndView mv = adapter.handle(req, res, handler);
        InternalResourceView view = viewResolver.resolve(mv.getViewName());
        view.render(dos, req, res);
        //then
        assertEquals("text/css",res.getContentType());
    }
}
