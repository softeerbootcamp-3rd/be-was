package handler;

import html.HTMLGenerator;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpResponseFactory;
import model.User;

import static http.method.HttpMethod.*;


public class HomeRequestHandler implements RequestHandler{
    @Override
    public HttpResponse handle(HttpRequest httpRequest, String findSessionId){
        String uri = httpRequest.getUri();
        if ("/".equals(uri)) {
            uri = "/index.html";
        }
		if (GET.equals(httpRequest.getMethod())) {
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
}
