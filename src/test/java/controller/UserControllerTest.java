package controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserControllerTest {

    private final UserController userController = UserController.getInstance();

    @Test
    @DisplayName("요청 URI에 따라 해당하는 컨트롤러를 선택하는지 확인")
    void route() {
        // given
        String pathUserForm = "/user/form";
        String pathUserList = "/user/list";
        String pathUserLogin = "/user/login";
        String pathUserLoginFailed = "/user/login_failed";
        String pathUserProfile = "/user/profile";
        String pathUserCreate = "/user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net";

        // when
        String resultUserForm = userController.route(pathUserForm);
        String resultUserList = userController.route(pathUserList);
        String resultUserLogin = userController.route(pathUserLogin);
        String resultUserLoginFailed = userController.route(pathUserLoginFailed);
        String resultUserProfile = userController.route(pathUserProfile);
        String resultUserCreate = userController.route(pathUserCreate);

        // then
        assertThat(resultUserForm).isEqualTo("200 " + pathUserForm);
        assertThat(resultUserList).isEqualTo("200 " + pathUserList);
        assertThat(resultUserLogin).isEqualTo("200 " + pathUserLogin);
        assertThat(resultUserLoginFailed).isEqualTo("200 " + pathUserLoginFailed);
        assertThat(resultUserProfile).isEqualTo("200 " + pathUserProfile);
        assertThat(resultUserCreate).isEqualTo("302 /index.html");
    }
}