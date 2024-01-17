package controller;

import data.RequestData;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

public class RequestDataControllerTest {

    // URL에 해당하는 데이터가 있는 경우
    @Test
    @DisplayName("html 리소스 라우팅 테스트")
    public void routeRequest_ValidResource() throws IOException {
        // Given
        String pathHome = "/";
        String pathIndex = "/index.html";
        String pathForm = "/user/form.html";
        String pathList = "/user/list.html";
        String pathLogin = "/user/login.html";
        String pathProfile = "/user/profile.html";

        // When
        Map<String, String> map = new HashMap<>();
        String redirectHome = RequestDataController.routeRequest(pathHome, new RequestData("GET", pathHome, "HTTP/1.1", map));
        String redirectIndex = RequestDataController.routeRequest(pathIndex, new RequestData("GET", pathIndex, "HTTP/1.1", map));
        String redirectForm = RequestDataController.routeRequest(pathForm, new RequestData("GET", pathForm, "HTTP/1.1", map));
        String redirectList = RequestDataController.routeRequest(pathList, new RequestData("GET", pathList, "HTTP/1.1", map));
        String redirectLogin = RequestDataController.routeRequest(pathLogin, new RequestData("GET", pathLogin, "HTTP/1.1", map));
        String redirectProfile = RequestDataController.routeRequest(pathProfile, new RequestData("GET", pathProfile, "HTTP/1.1", map));

        // Then
        assertThat(redirectHome).isEqualTo("/index.html");
        assertThat(redirectIndex).isNull();
        assertThat(redirectForm).isNull();
        assertThat(redirectList).isNull();
        assertThat(redirectLogin).isNull();
        assertThat(redirectProfile).isNull();
    }
}
