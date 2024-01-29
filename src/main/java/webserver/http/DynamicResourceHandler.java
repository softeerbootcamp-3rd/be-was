package webserver.http;

import db.H2Database;
import db.PostRepository;
import db.SessionManager;
import db.UserRepository;
import model.Post;
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

        Collection<Post> allPost = PostRepository.findAll();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<ul class =\"list\">");
        allPost.forEach(post -> {
            stringBuilder.append("<li>");
            stringBuilder.append("<div class='wrap'>");
            stringBuilder.append("<div class='main'>");
            stringBuilder.append("<strong class='subject'><a href='./qna/show.html'>").append(post.getTitle()).append("</a></strong>");
            stringBuilder.append("<div class='auth-info'>");
            stringBuilder.append("<i class='icon-add-comment'></i>");
            stringBuilder.append("<span class='time'>").append(post.getCreatedTime()).append("</span>");
            stringBuilder.append("<a href='./user/profile.html' class='author'>").append(post.getWriter()).append("</a>");
            stringBuilder.append("</div>");
            stringBuilder.append("<div class='reply' title='댓글'>");
            stringBuilder.append("<i class='icon-reply'></i>");
            stringBuilder.append("<span class='point'>").append(post.getCommentCount()).append("</span>");
            stringBuilder.append("</div>");
            stringBuilder.append("</div>");
            stringBuilder.append("</div>");
            stringBuilder.append("</li>");
        });
        stringBuilder.append("</ul>");

        StringBuilder replacer = new StringBuilder(responseContent);
        Pattern pattern = Pattern.compile("(?s)<ul class=\"list\"></ul>");
        Matcher matcher = pattern.matcher(replacer);

        if (matcher.find()) {
            replacer.replace(matcher.start(), matcher.end(), stringBuilder.toString());
        }

        if(!LoginChecker.loginCheck(request)){
            response.setResponseBody(responseContent.getBytes());
            return;
        }

        String sessionVal = request.getRequestHeader().get("Cookie").split("=")[1];
        User curUser = SessionManager.findUserById(sessionVal);
        //로그인 됐을시 이름으로 변경 로직
        responseContent = responseContent.replace("<li><a href=\"user/login.html\" role=\"button\">로그인</a></li>", "<li><a>"+curUser.getName() +"</a></li>");
        response.setResponseBody(responseContent.getBytes());
    }

    private void userListAPIFunction(Request request, Response response) {
        Collection<User> allUser = UserRepository.findAll();
        StringBuilder responseContent = new StringBuilder();
        responseContent.append("<div><ul>");
        allUser.forEach(user -> responseContent.append("<li><p>").append(user.getUserId()).append("</p></li>"));
        responseContent.append("</ul></div>");
        String stringContent = responseContent.toString();
        response.setResponseBody(stringContent.getBytes());
    }

    private void userListFunction(Request request, Response response){
        Collection<User> allUser = UserRepository.findAll();
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
