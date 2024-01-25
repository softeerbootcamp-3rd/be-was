package handler;

import http.HttpRequest;
import http.HttpResponse;
import db.Database;
import model.User;
import session.SessionManager;
import http.HttpResponseFactory;
import util.QueryStringParser;

import java.util.Map;
import java.util.Optional;

public class UserRequestHandler implements RequestHandler{
    @Override
    public HttpResponse handle(HttpRequest httpRequest){
        String uri = httpRequest.getUri();
        if (uri.equals("/user/create")) { // 회원가입
			Map<String, String> queryParameters = QueryStringParser.getParameters(httpRequest.getBody());
            User user = User.create(queryParameters);
            Database.addUser(user);
			// index.html 으로 리다이렉트
			return HttpResponseFactory.get302HttpResponse("/index.html");
        }
		if (uri.equals("/user/login")) { // 로그인
			Map<String, String> queryParameters = QueryStringParser.getParameters(httpRequest.getBody());
			Optional<User> userOptional = Database.findUserById(queryParameters.get("userId"));

			if (userOptional.isPresent()) { //아이디가 있는 경우 비밀번호 일치하는지 확인
				User findUser = userOptional.get();
				String findUserId = findUser.getUserId();
				String findUserPassword = findUser.getPassword();

				if (verifyPassword(findUserPassword, queryParameters.get("password"))) {
					String sessionId = SessionManager.generateSessionId(findUserId);
					return HttpResponseFactory.get302HttpResponse("/index.html", sessionId);
				}
			}
			// 아이디가 없는 경우
			return HttpResponseFactory.get302HttpResponse("/user/login_failed.html");
		}
		return getHttpResponse(uri);
    }

	private static boolean verifyPassword(String findUserPassword, String password) {
		return findUserPassword.equals(password);
	}
}
