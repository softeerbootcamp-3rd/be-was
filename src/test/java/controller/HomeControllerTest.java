package controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.RequestUrl;

import static org.assertj.core.api.Assertions.assertThat;

class HomeControllerTest {

    private final HomeController homeController = HomeController.getInstance();

    @Test
    @DisplayName("요청 URI에 따라 해당하는 컨트롤러를 선택하는지 확인")
    void route() {
        // given
        String pathHome = RequestUrl.HOME.getUrl();

        // when
        String resultHome = homeController.route(pathHome);

        // then
        assertThat(resultHome).isEqualTo("302 /index.html");
    }
}