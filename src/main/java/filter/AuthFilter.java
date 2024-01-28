package filter;

import dto.HttpResponseDto;
import handler.DynamicResponseHandler;
import handler.StaticResponseHandler;
import model.http.Status;
import model.http.request.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import session.Session;
import util.FileDetector;
import util.HtmlParser;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

import static config.AppConfig.*;

public class AuthFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);
    private static final List<String> dynamicElements = List.of("/user/create", "/user/login", "/user/list.html", "/index.html", "/user/form.html", "/user/list.html", "/user/login_failed.html", "/user/profile.html", "/qna/form.html", "/qna/show.html", "/user/login.html");
    private final StaticResponseHandler staticResponseHandler;
    private final DynamicResponseHandler dynamicResponseHandler;
    private final FileDetector fileDetector;

    private static class AuthFilterHolder {
        public static final AuthFilter INSTANCE = new AuthFilter(staticResponseHandler(), dynamicResponseHandler(), fileDetector());
    }

    public static AuthFilter getInstance() {
        return AuthFilterHolder.INSTANCE;
    }

    public AuthFilter(StaticResponseHandler staticResponseHandler, DynamicResponseHandler dynamicResponseHandler, FileDetector fileDetector) {
        this.staticResponseHandler = staticResponseHandler;
        this.dynamicResponseHandler = dynamicResponseHandler;
        this.fileDetector = fileDetector;
    }

    @Override
    public void init() {
        logger.debug("필터가 생성되었습니다.");
    }

    @Override
    public void doFilter(HttpRequest httpRequest, HttpResponseDto httpResponseDto) {
        boolean isLogin = false;
        if (httpRequest.getHeaders().hasCookie()) {
            isLogin = checkLogin(httpRequest);
        }
        if (httpRequest.getStartLine().getPathUrl().startsWith("/user/list") && !isLogin) {
            httpResponseDto.setStatus(Status.REDIRECT);
            httpResponseDto.getOptionHeader().put("Location", "/user/login.html");
            return;
        }
        boolean isDynamic = dynamicElements.stream().anyMatch(httpRequest.getStartLine().getPathUrl()::equals);
        if (isDynamic) {
            logger.debug("동적인 response 전달");
            dynamicResponseHandler.handle(httpRequest, httpResponseDto);
        } else {
            logger.debug("정적인 response 전달");
            staticResponseHandler.handle(httpRequest, httpResponseDto);
        }
        if(httpRequest.getStartLine().getPathUrl().endsWith(".html")){
            handleMainRequest(httpRequest, httpResponseDto, isLogin);
        }
    }

    private void handleMainRequest(HttpRequest httpRequest, HttpResponseDto httpResponseDto, boolean isLogin) {
        HtmlParser htmlParser;
        if (httpResponseDto.getContent() == null) {
            htmlParser = new HtmlParser(new String(fileDetector.getFile(httpRequest.getStartLine().getPathUrl())));
        }
        else{
            byte[] decode = Base64.getDecoder().decode(httpResponseDto.getContent());
            htmlParser = new HtmlParser(new String(decode));
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<ul class=\"nav navbar-nav navbar-right\">");
        if(isLogin){
            stringBuilder.append("<li class=\"active\"><a href=\"../index.html\">Posts</a></li>")
                    .append("<li><a href=\"#\" role=\"button\">로그아웃</a></li>")
                    .append("<li><a href=\"#\" role=\"button\">개인정보수정</a></li>");
        }else{
            stringBuilder.append("<li class=\"active\"><a href=\"../index.html\">Posts</a></li>")
                    .append("<li><a href=\"../user/login.html\" role=\"button\">로그인</a></li>")
                    .append("<li><a href=\"../user/form.html\" role=\"button\">회원가입</a></li>");
        }
        stringBuilder.append("</ul>");
        htmlParser.appendContentById("navbar-collapse2", stringBuilder.toString());
        httpResponseDto.setContentType(fileDetector.getContentType(httpRequest.getHeaders().getAccept(), httpRequest.getStartLine().getPathUrl()));
        httpResponseDto.setContent(htmlParser.getHtml().getBytes());
    }

    private boolean checkLogin(HttpRequest httpRequest) {
        String sessionId = httpRequest.getHeaders().getUserSessionId();
        return Session.loginCheck(UUID.fromString(sessionId));
    }
    @Override
    public void destroy() {
        logger.debug("필터가 삭제됩니다.");
    }
}
