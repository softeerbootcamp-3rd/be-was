package webserver;

import controller.Controller;
import controller.DefaultController;
import controller.UserController;
import dto.HttpRequestDto;
import dto.HttpRequestDtoBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.net.Socket;

public class RequestHandlerTest {
    private final Socket mockSocket = Mockito.mock(Socket.class);
    private final RequestHandler requestHandler = new RequestHandler(mockSocket);

    @Test()
    @DisplayName("RequestHandler.mappingController() Test")
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
