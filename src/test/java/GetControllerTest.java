import db.Database;
import dto.Response;
import model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import webserver.HttpStatus;

public class GetControllerTest {

    @Test
    public void signupTest() {

        // given : 테스트에서 구체화하고자 하는 행동을 하기 전 테스트의 상태를 설명하는 부분
        // [ 유저 생성 ] 이 주어졌을 때
        User user1 = new User("softeer", "bootcamp", "softeer", "abc@abc.com");

        // when : 구체화하고자 하는 행동
        // [ 유저를 DB에 저장한 후, 유저를 조회 ] 했을 때
        Database.addUser(user1);
        User findUser = Database.findUserById("softeer");

        // then : 예상되는 변화를 확인, 검증
        // [ 아래 조건을 만족 ] 해야 한다.
        Assertions.assertThat(user1).isEqualTo(findUser);

    }

    @Test
    public void responseBuilderTest() {

        HttpStatus httpStatus = HttpStatus.OK;
        String contentType = "text/html";
        byte[] body = "Hello World".getBytes();

        // given : 테스트에서 구체화하고자 하는 행동을 하기 전 테스트의 상태를 설명하는 부분
        // [ 생성자를 통해 ResponseBuilder를 생성 ] 했을 때
        Response response = new Response(httpStatus, contentType, body);

        // when : 구체화하고자 하는 행동
        // [ Builder 패턴을 이용해 ResponseBuilder를 생성 ] 했을 때
        Response builder = new Response.Builder()
                                        .httpStatus(httpStatus)
                                        .contentType(contentType)
                                        .body(body)
                                        .build();

        // then : 예상되는 변화를 확인, 검증
        // [ 두 객체가 같은 내용을 갖고 있어야 ] 한다.
        Assertions.assertThat(builder.getHttpStatus()).isEqualTo(response.getHttpStatus());
        Assertions.assertThat(builder.getContentType()).isEqualTo(response.getContentType());
        Assertions.assertThat(builder.getBody()).isEqualTo(response.getBody());
    }

}