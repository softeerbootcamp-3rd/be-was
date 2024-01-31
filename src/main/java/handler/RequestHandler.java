package handler;

import db.Database;
import http.HttpRequest;
import http.HttpResponse;
import http.method.HttpMethod;
import model.User;
import session.SessionManager;
import util.FileUtils;
import http.HttpResponseFactory;
import util.UriParser;

import java.util.Optional;

import static http.status.HttpStatusCode.*;

public interface RequestHandler {
    HttpResponse handle(HttpRequest httpRequest, String findSessionId);

    default HttpResponse getHttpResponse(String uri){
        Optional<byte[]> body = FileUtils.readFile(uri);
        if (body.isPresent()) {
			return HttpResponseFactory.get200Response(uri, body.get());
        }
		//body가 empty라면 404 페이지 전송
		body = FileUtils.readFile("/error/404.html");
		//404 페이지를 찾을 수 없으면 "404 NOT FOUND" 문자열 전송
		return HttpResponseFactory.get404Response(uri, body.orElse(NOT_FOUND.toString().getBytes()));
    }

	default HttpResponse get405HttpResponse(HttpMethod method) {
		String uri = "/error/405.html";
		Optional<byte[]> body = FileUtils.readFile(uri);
		return HttpResponseFactory.get405Response(uri, body.orElse(METHOD_NOT_ALLOWED.toString().getBytes()), method);
	}

	default HttpResponse get400HttpResponse() {
		String uri = "/error/400.html";
		Optional<byte[]> body = FileUtils.readFile(uri);
		return HttpResponseFactory.get400Response(uri, body.orElse(BAD_REQUEST.toString().getBytes()));
	}
	default boolean isHTML(String uri) {
		String fileType = UriParser.getFileType(uri);
		return fileType.equals("html");
	}

	default User getUserBySession(String sessionId) {
		String userId = SessionManager.getUserIdBySessionId(sessionId);
		return Database.findUserById(userId);
	}
}
