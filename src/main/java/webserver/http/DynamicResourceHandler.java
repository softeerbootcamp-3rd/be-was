package webserver.http;

import db.H2Database;
import db.SessionManager;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.LoginChecker;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DynamicResourceHandler {
    private static final Logger logger = LoggerFactory.getLogger(DynamicResourceHandler.class);
    private final Map<String, BiConsumer<Request, Response>> resourceHandlers = new HashMap<>();

    public DynamicResourceHandler() {
        resourceHandlers.put("/index.html", this::indexFunction);
        resourceHandlers.put("/user/list", this::userListAPIFunction);
        resourceHandlers.put("/user/list.html", this::userListFunction);
    }

    private void indexFunction(Request request, Response response) {
        byte[] responseBody = response.getResponseBody();
        String responseContent;
        try {
            responseContent = new String(responseBody, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("Encoding Exception", e);
            responseContent = new String(responseBody);
        }
        String originLink = "<a href=\"./qna/form.html\" class=\"btn btn-primary pull-right\" role=\"button\">질문하기</a>";
        String newLink = "<a href=\"./board/write.html\" class=\"btn btn-secondary pull-right\" role=\"button\">글쓰기</a>";
        responseContent = responseContent.replace(originLink,originLink + newLink);

        if(!LoginChecker.loginCheck(request)){
            response.setResponseBody(responseContent.getBytes());
            return;
        }

        String sessionVal = request.getRequestHeader().get("Cookie").split("=")[1];
        User curUser = SessionManager.findUserById(sessionVal);

        responseContent = responseContent.replace("<li><a href=\"user/login.html\" role=\"button\">로그인</a></li>", "<li><a>"+curUser.getName() +"</a></li>");
        response.setResponseBody(responseContent.getBytes());
    }

    private void userListAPIFunction(Request request, Response response) {
        Collection<User> allUser = H2Database.findAll();
        StringBuilder responseContent = new StringBuilder();
        responseContent.append("<div><ul>");
        allUser.forEach(user -> responseContent.append("<li><p>").append(user.getUserId()).append("</p></li>"));
        responseContent.append("</ul></div>");
        String stringContent = responseContent.toString();
        response.setResponseBody(stringContent.getBytes());
    }

    private void userListFunction(Request request, Response response){
        Collection<User> allUser = H2Database.findAll();
        byte[] responseBody = response.getResponseBody();

        //byte[] -> String
        String responseContent;
        try {
            responseContent = new String(responseBody, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("Encoding Exception", e);
            responseContent = new String(responseBody);
        }

        StringBuilder stringBuilder = new StringBuilder();
        AtomicInteger counter = new AtomicInteger(1);
        stringBuilder.append("<tbody>");
        allUser.forEach(user -> {
            stringBuilder.append("<tr>");
            stringBuilder.append("<th scope='row'>").append(counter.getAndIncrement()).append("</th>");
            stringBuilder.append("<td>").append(user.getUserId()).append("</td>");
            stringBuilder.append("<td>").append(user.getName()).append("</td>");
            stringBuilder.append("<td>").append(user.getEmail()).append("</td>");
            stringBuilder.append("<td><a href='#' class='btn btn-success' role='button'>수정</a></td>");
            stringBuilder.append("</tr>");
        });
        stringBuilder.append("</tbody>");

        StringBuilder replacer = new StringBuilder(responseContent);
        Pattern pattern = Pattern.compile("(?s)<tbody>.*?</tbody>");
        Matcher matcher = pattern.matcher(replacer);

        if (matcher.find()) {
            replacer.replace(matcher.start(), matcher.end(), stringBuilder.toString());
        }

        response.setResponseBody(replacer.toString().getBytes());
    }

    public void handle(Request request, Response response) {
        if (resourceHandlers.containsKey(request.getRequestTarget())) {
            resourceHandlers.get(request.getRequestTarget()).accept(request, response);
        }
    }
}
