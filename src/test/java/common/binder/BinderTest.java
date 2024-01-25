package common.binder;

import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BinderTest {

    @Test
    @DisplayName("쿼리스트링 -> object 동적 바인딩 테스트")
    void bindQueryStringToObject() throws Exception {

        //given
        String queryString = "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net";

        //when
        User user = Binder.bindQueryStringToObject(queryString, User.class);

        //then
        assertThat(user.getUserId()).isEqualTo("javajigi");
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.getName()).isEqualTo("박재성");
        assertThat(user.getEmail()).isEqualTo("javajigi@slipp.net");
    }
}
