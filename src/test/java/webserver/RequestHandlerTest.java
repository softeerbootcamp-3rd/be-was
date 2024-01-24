package webserver;

import controller.Controller;
import controller.DefaultController;
import controller.UserController;
import dto.HttpRequestDto;
import dto.HttpRequestDtoBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.Socket;

public class RequestHandlerTest {
    private final RequestHandler requestHandler = new RequestHandler(new Socket());

    @Test()
    @DisplayName("mappingController(): 요청 URL에 따라 적절한 컨트롤러를 매핑해준다.")
    public void mappingControllerTest() {
        // given
        HttpRequestDto httpRequestDto = new HttpRequestDtoBuilder("GET", "/index.html", "HTTP/1.1").build();
        HttpRequestDto httpRequestDto2 = new HttpRequestDtoBuilder("GET", "/user", "HTTP/1.1").build();

        // when
        Controller controller = requestHandler.mappingController(httpRequestDto);
        Controller controller2 = requestHandler.mappingController(httpRequestDto2);

        // then
        Assertions.assertThat(controller).isInstanceOf(DefaultController.class);
        Assertions.assertThat(controller2).isInstanceOf(UserController.class);
    }
}
