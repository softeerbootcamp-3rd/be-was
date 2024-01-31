package handler;

import db.Database;
import http.HttpRequest;
import http.HttpResponse;
import http.builder.HttpRequestBuilder;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class UserRequestHandlerTest {

	@AfterEach
	void after() {
		Database.removeAll();
	}
	@Test
	@DisplayName("회원가입 요청시 사용자 정보 db 저장 성공 테스트")
	void 회원가입시_db_저장_O() {
		//given
		String body = "userId=hello&password=sdd&name=swk&email=test@naver.com";
		String session = UUID.randomUUID().toString();

		HttpRequest httpRequest = new HttpRequestBuilder()
			.method("POST")
			.uri("/user/create")
			.body(body)
			.build();

		RequestHandler requestHandler = new UserRequestHandler();

		//when
		HttpResponse httpResponse = requestHandler.handle(httpRequest ,session);
		User user = Database.findUserById("hello");

		//then
		assertThat(user).isNotNull();
		assertThat(user.getPassword()).isEqualTo("sdd");
		assertThat(user.getName()).isEqualTo("swk");
		assertThat(user.getEmail()).isEqualTo("test@naver.com");
	}

	@Test
	@DisplayName("회원가입이 완료된 후 로그인 성공 테스트")
	void 회원가입_O_로그인_O() {
		//given
		initUserDB();
		String session = UUID.randomUUID().toString();

		//when
		RequestHandler requestHandler = new UserRequestHandler();

		HttpRequest loginRequest = getLoginRequest("user", "userPW");
		HttpResponse httpResponse = requestHandler.handle(loginRequest, session);
		Map<String, String> headerFields = httpResponse.getHeaderFields();
		String uri = headerFields.get("Location");

		//then
		assertThat(uri).isEqualTo("/index.html");


	}

	@Test
	@DisplayName("회원가입이 완료된 후 잘못된 비밀번호 입력으로 로그인 실패 테스트")
	void 회원가입_O_로그인_X() {
		//given
		initUserDB();
		String session = UUID.randomUUID().toString();

		//when
		RequestHandler requestHandler = new UserRequestHandler();

		HttpRequest loginRequest = getLoginRequest("user", "wrongPW");
		HttpResponse httpResponse = requestHandler.handle(loginRequest, session);
		Map<String, String> headerFields = httpResponse.getHeaderFields();
		String uri = headerFields.get("Location");

		//then
		assertThat(uri).isEqualTo("/user/login_failed.html");
	}

	@Test
	@DisplayName("회원가입을 하지 않고 로그인을 시도하여 로그인 실패 테스트")
	void 회원가입_X_로그인_X() {
		//given
		String session = UUID.randomUUID().toString();
		//when
		RequestHandler requestHandler = new UserRequestHandler();

		HttpRequest loginRequest = getLoginRequest("user", "userPW");
		HttpResponse httpResponse = requestHandler.handle(loginRequest, session);
		Map<String, String> headerFields = httpResponse.getHeaderFields();
		String uri = headerFields.get("Location");

		//then
		assertThat(uri).isEqualTo("/user/login_failed.html");
	}


	private static HttpRequest getLoginRequest(String id, String password) {
		String body = "userId=" + id+ "&" + "password=" + password;

		HttpRequest httpRequest = new HttpRequestBuilder()
			.method("POST")
			.uri("/user/login")
			.body(body)
			.build();
		return httpRequest;
	}

	private static void initUserDB() {
		Map<String, String> queryParameters = new HashMap<>();
		queryParameters.put("userId", "user");
		queryParameters.put("password", "userPW");
		queryParameters.put("name", "kim");
		queryParameters.put("email", "test@email.com");
		User newUser = User.create(queryParameters);

		Database.addUser(newUser);
	}
}
