package controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.RequestUrl;

import static org.assertj.core.api.Assertions.assertThat;

class UserControllerTest {

    private final UserController userController = UserController.getInstance();

    @Test
    @DisplayName("요청 URI에 따라 해당하는 컨트롤러를 선택하는지 확인")
    void route() {
        // given
        String pathUserForm = RequestUrl.USER_FORM.getUrl();
        String pathUserList = RequestUrl.USER_LIST.getUrl();
        String pathUserLogin = RequestUrl.USER_LOGIN.getUrl();
        String pathUserLoginFailed = RequestUrl.USER_LOGIN_FAILED.getUrl();
        String pathUserProfile = RequestUrl.USER_PROFILE.getUrl();
        String pathUserCreate = RequestUrl.USER_CREATE.getUrl() + "?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net";

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