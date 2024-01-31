package handler;

import exception.InvalidSignUpException;
import html.HTMLGenerator;
import http.HttpRequest;
import http.HttpResponse;
import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import session.SessionManager;
import http.HttpResponseFactory;
import util.FileUtils;
import util.QueryStringParser;
import webserver.RequestController;

import java.util.Map;
import java.util.Optional;

import static http.method.HttpMethod.*;
import static http.status.HttpStatusCode.BAD_REQUEST;
import static http.status.HttpStatusCode.NOT_FOUND;

public class UserRequestHandler implements RequestHandler{

	private static final Logger logger = LoggerFactory.getLogger(UserRequestHandler.class);
    @Override
    public HttpResponse handle(HttpRequest httpRequest, String findSessionId){
        String uri = httpRequest.getUri();
        if ("/user/create".equals(uri)) { // 회원가입
			if(POST.equals(httpRequest.getMethod())){
				try {
					Map<String, String> queryParameters = QueryStringParser.getParameters(httpRequest.getBody());
					User user = User.create(queryParameters);
					Database.addUser(user);
					// index.html 으로 리다이렉트
					return HttpResponseFactory.get302Response("/index.html");
				} catch (InvalidSignUpException e) {
					logger.error("{}", e.getMessage());
					return get400HttpResponse();
				}

			}
			return get405HttpResponse(POST);
		}

		if ("/user/login".equals(uri)) { // 로그인
			if (POST.equals(httpRequest.getMethod())) {
				try {
					Map<String, String> queryParameters = QueryStringParser.getParameters(httpRequest.getBody());
					User findUser = Database.findUserById(queryParameters.get("userId"));

					if (findUser != null) { //아이디가 있는 경우 비밀번호 일치하는지 확인
						String findUserId = findUser.getUserId();
						String findUserPassword = findUser.getPassword();

						if (verifyPassword(findUserPassword, queryParameters.get("password"))) {
							String sessionId = SessionManager.generateSessionId(findUserId);
							return HttpResponseFactory.get302LoginResponse("/index.html", sessionId);
						}
					}
					// 아이디가 없는 경우
					return HttpResponseFactory.get302Response("/user/login_failed.html");
				} catch (InvalidSignUpException e) {
					logger.error("{}", e.getMessage());
					return get400HttpResponse();
				}

			}
			return get405HttpResponse(POST);
		}
		if (GET.equals(httpRequest.getMethod())) {
			if ("/user/list.html".equals(uri)) { // 사용자 목록
				User findUser = getUserBySession(findSessionId);
				if (findUser != null) {
					byte[] userListHTML = HTMLGenerator.getUserListHTML(findUser.getName());
					return HttpResponseFactory.get200Response(uri, userListHTML);
				}
				return HttpResponseFactory.get302Response("/user/login.html");
			}

			if (isHTML(uri)) {
				User findUser = getUserBySession(findSessionId);
				if (findUser != null) { // 세션 ID로 User 찾은 경우
					byte[] HTML = HTMLGenerator.getHTML(findUser.getName(), uri);
					return HttpResponseFactory.get200Response(uri, HTML);
				}
			}
			return getHttpResponse(uri);
		}
		return get405HttpResponse(GET);
    }

	private static boolean verifyPassword(String findUserPassword, String password) {
		return findUserPassword.equals(password);
	}
}
