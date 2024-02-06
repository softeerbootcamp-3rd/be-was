package util;

import controller.Controller;
import controller.DefaultController;
import controller.UserController;
import dto.HttpRequestDto;
import dto.HttpRequestDtoBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ControllerMapperTest {
    @Test
    @DisplayName("mappingController(): uri에 따라서 적절한 컨트롤러를 반환한다")
    public void mappingControllerTest() {
        // given
        HttpRequestDto rootUriRequest = new HttpRequestDtoBuilder("GET", "/index.html", "HTTP/1.1").build();
        HttpRequestDto userUriRequest = new HttpRequestDtoBuilder("GET", "/user", "HTTP/1.1").build();

        // when
        Controller defaultController = ControllerMapper.mappingController(rootUriRequest);
        Controller userController = ControllerMapper.mappingController(userUriRequest);

        // then
        Assertions.assertThat(defaultController).isInstanceOf(DefaultController.class);
        Assertions.assertThat(userController).isInstanceOf(UserController.class);
    }
}
