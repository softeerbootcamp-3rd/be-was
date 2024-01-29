package webserver.http;

import db.Database;
import db.SessionManager;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.LoginChecker;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class DynamicResourceHandler {
    private static final Logger logger = LoggerFactory.getLogger(DynamicResourceHandler.class);
    private final Map<String, BiConsumer<Request, Response>> resourceHandlers = new HashMap<>();

    public DynamicResourceHandler() {
        resourceHandlers.put("/index.html", this::indexFunction);
        resourceHandlers.put("/user/list", this::userListFunction);
    }

    private void indexFunction(Request request, Response response) {
        byte[] responseBody = response.getResponseBody();

        if(!LoginChecker.loginCheck(request)){
            response.setResponseBody(responseBody);
            return;
        }

        String sessionVal = request.getRequestHeader().get("Cookie").split("=")[1];
        User curUser = SessionManager.findUserById(sessionVal);
        String responseContent;
        try {
            responseContent = new String(responseBody, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("Encoding Exception", e);
            responseContent = new String(responseBody);
        }
        responseContent = responseContent.replace("<li><a href=\"user/login.html\" role=\"button\">로그인</a></li>", "<li><a>"+curUser.getName() +"</a></li>");
        response.setResponseBody(responseContent.getBytes());
    }

    private void userListFunction(Request request, Response response) {
        Collection<User> allUser = Database.findAll();
        StringBuilder responseContent = new StringBuilder();
        responseContent.append("<div><ul>");
        allUser.forEach(user -> {
            responseContent.append("<li><p>").append(user.getUserId()).append("</p></li>");
        });
        responseContent.append("</ul></div>");
        String stringContent = responseContent.toString();
        response.setResponseBody(stringContent.getBytes());
    }

    public void handle(Request request, Response response) {
        if (resourceHandlers.containsKey(request.getRequestTarget())) {
            resourceHandlers.get(request.getRequestTarget()).accept(request, response);
        }
    }
}
